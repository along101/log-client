package com.along101.logclient.metrics;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yinzuolong on 2017/6/5.
 */
public class ConsoleMetricReporter implements MetricReporter {

    private String timeStampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private final MetricCopyExporter metricCopyExporter;
    private SimpleDateFormat sdf = new SimpleDateFormat(timeStampFormat);

    public ConsoleMetricReporter(MetricService metricService) {
        this.metricCopyExporter = new MetricCopyExporter("", metricService.getMetricRepository());
    }

    @Override
    public void report() {
        List<Metric<?>> metrics = this.metricCopyExporter.export();
        for (Metric<?> metric : metrics) {
            System.out.printf("%s  %s  %s%n",
                    sdf.format(metric.getTimestamp()), metric.getName(), metric.getValue());
        }
    }
}
