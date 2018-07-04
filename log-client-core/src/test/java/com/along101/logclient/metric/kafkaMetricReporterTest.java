package com.along101.logclient.metric;

import com.along101.logclient.metrics.KafkaMetricReporter;
import com.along101.logclient.metrics.MetricContext;
import com.along101.logclient.transport.LazyProducer;
import com.along101.logclient.utils.IPUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/6/5.
 */
public class kafkaMetricReporterTest {

    private String bootstrap = "localhost:9092";

    @Test
    public void testReport() throws Exception {
        MetricContext.metricService.increment("counter1", 1);
        MetricContext.metricService.increment("counter1", 1);

        MetricContext.metricService.submit("gauge1", 2.5);
        KafkaMetricReporter kafkaMetricReporter = new KafkaMetricReporter(MetricContext.metricService,
                createLazyProducer(),
                "");
        kafkaMetricReporter.addTag("appId", "kafkaMetricReporterTest");
        kafkaMetricReporter.addTag("hostIp", IPUtil.getLocalIP());
        kafkaMetricReporter.setTopic("framework.metric");
        kafkaMetricReporter.report();
    }

    @Test
    public void testSchedule() throws Exception {

        MetricContext.metricService.increment("counter1", 1);
        MetricContext.metricService.increment("counter1", 1);

        MetricContext.metricService.submit("gauge1", 2.5);
        KafkaMetricReporter kafkaMetricReporter = new KafkaMetricReporter(MetricContext.metricService,
                createLazyProducer(),
                "");
        kafkaMetricReporter.addTag("appId", "kafkaMetricReporterTest");
        kafkaMetricReporter.addTag("hostIp", IPUtil.getLocalIP());
        kafkaMetricReporter.setTopic("framework.metric");
        kafkaMetricReporter.setPeriod(2);
        kafkaMetricReporter.start();
        for (int i = 0; i < 10; i++) {
            MetricContext.metricService.increment("counter1", 2);
            MetricContext.metricService.submit("gauge1", i);
            Thread.sleep(1000);
        }
        kafkaMetricReporter.stop();
    }

    private LazyProducer createLazyProducer() {
        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", bootstrap);
        config.put("retries", "0");
        config.put("max.block.ms", "3000");
        config.put("acks", "all");
        config.put("batch.size", "16384");
        config.put("linger.ms", "1");
        config.put("buffer.memory", "33554432");
        LazyProducer lazyProducer = new LazyProducer(config);
        return lazyProducer;
    }

}
