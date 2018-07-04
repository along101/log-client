package com.along101.logclient.metric;

import com.along101.logclient.metrics.ConsoleMetricReporter;
import com.along101.logclient.metrics.MetricContext;
import org.junit.Test;

/**
 * Created by yinzuolong on 2017/6/5.
 */
public class ConsoleMetricReporterTest {

    @Test
    public void testReport() throws Exception {
        MetricContext.metricService.increment("counter1", 1);
        MetricContext.metricService.increment("counter1", 1);

        MetricContext.metricService.submit("gauge1", 2.5);
        ConsoleMetricReporter reporter = new ConsoleMetricReporter(MetricContext.metricService);
        reporter.report();
    }

    @Test
    public void testProfile() throws Exception {
        long n = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            MetricContext.metricService.increment("counter1", 1);
            MetricContext.metricService.increment("counter1", 1);
            MetricContext.metricService.submit("gauge1", 2.5);
        }
        MetricContext.metricService.submit("testProfile", System.currentTimeMillis() - n);
        ConsoleMetricReporter reporter = new ConsoleMetricReporter(MetricContext.metricService);
        reporter.report();
    }
}
