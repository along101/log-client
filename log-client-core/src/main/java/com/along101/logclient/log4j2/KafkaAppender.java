package com.along101.logclient.log4j2;

import com.along101.logclient.bean.LogMessage;
import com.along101.logclient.metrics.KafkaMetricReporter;
import com.along101.logclient.metrics.MetricContext;
import com.along101.logclient.transport.FailedTransportCallback;
import com.along101.logclient.transport.KafkaTransporter;
import com.along101.logclient.transport.LazyProducer;
import com.along101.logclient.utils.IPUtil;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by yinzuolong on 2017/6/15.
 */
@Plugin(name = "Along101", category = Node.CATEGORY, elementType = "appender", printObject = true)
public final class KafkaAppender extends AbstractAppender implements FailedTransportCallback {

    private static String METRIC_PREFIX = "logclient";
    private KafkaMetricReporter metricReporter;
    private KafkaTransporter kafkaTransporter;
    private String appId;
    private boolean manualStart;

    protected KafkaAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
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
        kafkaTransporter.start();
        //发送测试
        kafkaTransporter.sendTest(3000, appId);
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
    public void append(LogEvent event) {
        if (event.getLoggerName() != null && event.getLoggerName().startsWith("org.apache.kafka")) {
            LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), getName());
        } else {
            try {
                tryAppend(event);
            } catch (final Exception e) {
                error("Unable to write to Kafka in appender [" + getName() + "]", event, e);
            }
        }
    }

    private void tryAppend(LogEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        if (event == null || !isStarted())
            return;
        String layoutMsg = (String) this.getLayout().toSerializable(event);
        LogMessage logMessage = LogMessageBuilder.build(event);
        logMessage.setAppId(appId);
        logMessage.setLayoutMessage(layoutMsg);
        kafkaTransporter.sendKafka(logMessage);
        MetricContext.metricService.increment("append", 1);
    }


    @PluginFactory
    public static KafkaAppender createAppender(
            @PluginElement("Layout") final Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @Required(message = "No name provided for KafkaAppender") @PluginAttribute("name") final String name,
            @Required(message = "No appId provided for KafkaAppender") @PluginAttribute("appId") final String appId,
            @Required(message = "No topic provided for KafkaAppender") @PluginAttribute("topic") final String topic,
            @Required(message = "No reportTopic provided for KafkaAppender") @PluginAttribute("reportTopic") final String reportTopic,
            @PluginAttribute("syncSend") final boolean syncSend,
            @PluginAttribute("manualStart") final boolean manualStart,
            @PluginAttribute(value = "sendTimeout", defaultInt = 3) final int sendTimeout,
            @PluginAttribute(value = "reportPeriod", defaultInt = 60) final int reportPeriod,
            @PluginElement("Properties") final Property[] properties,
            @PluginConfiguration final Configuration configuration) {
        KafkaAppender appender = new KafkaAppender(name, filter, layout);
        appender.appId = appId;
        appender.manualStart = manualStart;
        Map<String, Object> config = new HashMap<>();
        for (Property property : properties) {
            config.put(property.getName(), property.getValue());
        }

        LazyProducer lazyProducer = new LazyProducer(config);
        KafkaTransporter kafkaTransporter = new KafkaTransporter(topic, syncSend, lazyProducer, appender);
        kafkaTransporter.setSendTimeout(sendTimeout);
        appender.kafkaTransporter = kafkaTransporter;

        KafkaMetricReporter metricReporter = new KafkaMetricReporter(MetricContext.metricService,
                lazyProducer, METRIC_PREFIX);
        metricReporter.setTopic(reportTopic);
        metricReporter.addTag("IP", IPUtil.getLocalIP());
        metricReporter.addTag("appId", appId);
        metricReporter.addTag("agent", "log4j2");
        metricReporter.setPeriod(reportPeriod);
        appender.metricReporter = metricReporter;
        return appender;
    }

    @Override
    public void onFailedDelivery(LogMessage evt, Throwable throwable) {
        //TODO 失败处理
    }
}
