package com.along101.logclient.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.along101.logclient.transport.FailedTransportCallback;
import com.along101.logclient.transport.KafkaTransporter;
import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.metrics.KafkaMetricReporter;
import com.along101.logclient.metrics.MetricContext;
import com.along101.logclient.transport.LazyProducer;
import com.along101.logclient.utils.IPUtil;

import java.util.HashSet;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public class KafkaAppender extends KafkaAppenderConfig implements FailedTransportCallback {

    private static HashSet<KafkaAppender> kafkaAppenders = new HashSet<>();
    private static String METRIC_PREFIX = "logclient";
    private KafkaTransporter kafkaTransporter;
    private KafkaMetricReporter metricReporter;

    public KafkaAppender() {
        kafkaAppenders.add(this);
    }

    public static HashSet<KafkaAppender> getKafkaAppenders() {
        return kafkaAppenders;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (event == null || !isStarted())
            return;
        LogMessage logMessage = LogMessageBuilder.build(event);
        logMessage.setAppId(appId);
        logMessage.setLayoutMessage(this.layout.doLayout(event));
        this.kafkaTransporter.sendKafka(logMessage);
        MetricContext.metricService.increment("append", 1);
    }

    @Override
    public void start() {
        if (isStarted())
            return;
        //手动启动
        if (manualStart) {
            return;
        }
        doStart();
    }

    public void doStart() {
        LazyProducer lazyProducer = new LazyProducer(this.producerConfig);
        kafkaTransporter = new KafkaTransporter(this.topic, this.syncSend, lazyProducer, this);
        kafkaTransporter.start();
        //发送测试
        kafkaTransporter.sendTest(3000, appId);

        metricReporter = new KafkaMetricReporter(MetricContext.metricService,
                lazyProducer, METRIC_PREFIX);
        metricReporter.setTopic(this.getReportTopic());
        metricReporter.addTag("IP", IPUtil.getLocalIP());
        metricReporter.addTag("appId", this.getAppId());
        metricReporter.addTag("agent", "logback");
        metricReporter.setPeriod(this.reportPeriod);
        metricReporter.start();
        super.start();
    }

    @Override
    public void stop() {
        if (!isStarted())
            return;
        kafkaTransporter.stop();
        metricReporter.stop();
        super.stop();
    }

    @Override
    public void onFailedDelivery(LogMessage evt, Throwable throwable) {
        //TODO 失败处理
    }
}
