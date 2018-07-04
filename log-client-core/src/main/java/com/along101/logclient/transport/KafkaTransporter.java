package com.along101.logclient.transport;

import com.alibaba.fastjson.JSON;
import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.logback.LogMessageBuilder;
import com.along101.logclient.metrics.MetricContext;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public class KafkaTransporter {

    private LazyProducer lazyProducer;
    private String topic;
    private boolean syncSend;
    private int sendTimeout = 3;
    private FailedTransportCallback failedTransportCallback;
    private boolean started;

    public KafkaTransporter(String topic, boolean syncSend, LazyProducer lazyProducer,
                            FailedTransportCallback failedTransportCallback) {
        this.topic = topic;
        this.syncSend = syncSend;
        this.failedTransportCallback = failedTransportCallback;
        this.lazyProducer = lazyProducer;
    }

    public void start() {
        this.lazyProducer.get();
        this.started = true;
    }

    public boolean sendTest(long timeoutMs, String message) {
        LogMessage logMessage = LogMessageBuilder.buildTest();
        logMessage.setMessage(message);
        String json = JSON.toJSONString(logMessage);
        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, json.getBytes(StandardCharsets.UTF_8));
        long remainTimeout = timeoutMs;
        long begin = System.currentTimeMillis();
        while (remainTimeout > 0) {
            try {
                this.lazyProducer.get().send(record).get(remainTimeout, TimeUnit.MILLISECONDS);
                return true;
            } catch (Exception e) {
                try {
                    wait(100);
                } catch (Exception e1) {
                }
            }
            long elapsed = System.currentTimeMillis() - begin;
            remainTimeout = timeoutMs - elapsed;
        }
        return false;
    }

    public void stop() {
        this.lazyProducer.destroy();
    }

    public boolean isStarted() {
        return started;
    }

    private void onFailed(LogMessage logMessage, Throwable t) {
        if (this.failedTransportCallback != null) {
            this.failedTransportCallback.onFailedDelivery(logMessage, t);
        }
    }

    public void sendKafka(final LogMessage logMessage) {
        if (!this.started) {
            return;
        }
        try {
            String json = JSON.toJSONString(logMessage);
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, json.getBytes(StandardCharsets.UTF_8));
            Future<RecordMetadata> response = this.lazyProducer.get().send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        onFailed(logMessage, exception);
                        MetricContext.metricService.increment("send.exception", 1);
                    }
                }
            });
            if (response == null) {
                MetricContext.metricService.increment("discard", 1);
                return;
            }
            if (this.syncSend) {
                try {
                    response.get(this.sendTimeout, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    onFailed(logMessage, e);
                    MetricContext.metricService.increment("send.InterruptedException", 1);
                } catch (ExecutionException e) {
                    onFailed(logMessage, e);
                    MetricContext.metricService.increment("send.ExecutionException", 1);
                } catch (TimeoutException e) {
                    onFailed(logMessage, e);
                    MetricContext.metricService.increment("send.TimeoutException", 1);
                }
            }
        } catch (Exception e) {
            onFailed(logMessage, e);
            MetricContext.metricService.increment("send.Exception", 1);
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isSyncSend() {
        return syncSend;
    }

    public void setSyncSend(boolean syncSend) {
        this.syncSend = syncSend;
    }

    public int getSendTimeout() {
        return sendTimeout;
    }

    public void setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public FailedTransportCallback getFailedTransportCallback() {
        return failedTransportCallback;
    }

    public void setFailedTransportCallback(FailedTransportCallback failedTransportCallback) {
        this.failedTransportCallback = failedTransportCallback;
    }
}
