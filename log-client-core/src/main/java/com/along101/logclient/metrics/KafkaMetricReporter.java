package com.along101.logclient.metrics;

import com.alibaba.fastjson.JSON;
import com.along101.logclient.transport.KafkaTransporter;
import com.along101.logclient.bean.MetricMessage;
import com.along101.logclient.transport.LazyProducer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by yinzuolong on 2017/6/1.
 */
public class KafkaMetricReporter implements MetricReporter {
    private final MetricService metricService;
    private final MetricCopyExporter metricCopyExporter;
    private final ScheduledExecutorService executor;
    private Map<String, String> tags;
    private LazyProducer lazyProducer;
    private String topic = "framework.metric";
    private long period = 60;
    private TimeUnit unit = TimeUnit.SECONDS;

    public KafkaMetricReporter(MetricService metricService, LazyProducer lazyProducer, String prefix) {
        this.metricService = metricService;
        this.metricCopyExporter = new MetricCopyExporter(prefix, metricService.getMetricRepository());
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.lazyProducer = lazyProducer;
    }

    @Override
    public void report() {
        List<Metric<?>> metrics = this.metricCopyExporter.export();
        List<MetricMessage> metricMessages = new ArrayList<>();
        for (Metric<?> metric : metrics) {
            MetricMessage metricMessage = new MetricMessage(metric.getName(),
                    this.tags,
                    metric.getTimestamp().getTime(),
                    metric.getValue());
            metricMessages.add(metricMessage);
        }
        sendKafka(metricMessages);
    }

    public void sendKafka(List<MetricMessage> metricMessages) {
        try {
            String json = JSON.toJSONString(metricMessages);
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, json.getBytes(StandardCharsets.UTF_8));
            Future<RecordMetadata> response = this.lazyProducer.get().send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        metricService.increment("logclient.metric.report.error", 1);
                    }
                }
            });
            if (response == null) {
                metricService.increment("logclient.metric.report.discard", 1);
                return;
            }
        } catch (Exception e) {
            metricService.increment("logclient.metric.report.exception", 1);
        }
    }

    public void start() {
        this.lazyProducer.get();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    report();
                } catch (Exception ex) {
                    System.err.println(getClass().getSimpleName() + ": report error " + ex.getMessage());
                }
            }
        }, period, period, unit);
    }

    public void stop() {
        this.lazyProducer.destroy();
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.err.println(getClass().getSimpleName() + ": ScheduledExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void addTag(String name, String value) {
        if (this.tags == null) {
            this.tags = new HashMap<>();
        }
        this.tags.put(name, value);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}
