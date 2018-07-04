package com.along101.logclient.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/5/25.
 */
public abstract class KafkaAppenderConfig extends UnsynchronizedAppenderBase<ILoggingEvent> {

    protected Layout<ILoggingEvent> layout;
    protected boolean manualStart = false;
    protected Map<String, Object> producerConfig = new HashMap<>();
    protected String topic = "framework.log";
    protected String reportTopic = "framework.log.report";
    protected int reportPeriod = 60;
    protected String appId = null;
    protected String bootstrapServers;
    protected boolean syncSend = false;
    protected int sendTimeout = 3;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean isManualStart() {
        return manualStart;
    }

    public void setManualStart(boolean manualStart) {
        this.manualStart = manualStart;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public void addProducerConfig(String keyValue) {
        String[] split = keyValue.split("=", 2);
        if (split.length == 2)
            this.producerConfig.put(split[0], split[1]);
    }

    public void putProducerConfig(String key, String value) {
        this.producerConfig.put(key, value);
    }

    public void putProducerConfigs(Map<String, String> configs) {
        this.producerConfig.putAll(configs);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        this.producerConfig.put("bootstrap.servers", bootstrapServers);
    }

    public String getReportTopic() {
        return reportTopic;
    }

    public void setReportTopic(String reportTopic) {
        this.reportTopic = reportTopic;
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

    public int getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(int reportPeriod) {
        this.reportPeriod = reportPeriod;
    }
}