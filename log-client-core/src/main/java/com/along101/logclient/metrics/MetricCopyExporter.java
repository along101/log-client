package com.along101.logclient.metrics;

import com.along101.logclient.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yinzuolong on 2017/6/5.
 */
public class MetricCopyExporter {

    private final String prefix;
    private boolean ignoreTimestamps = false;
    private boolean sendLatest = true;
    private MetricRepository metricRepository;
    private volatile AtomicBoolean processing = new AtomicBoolean(false);
    private ConcurrentMap<String, Long> counts = new ConcurrentHashMap<String, Long>();
    private Date latestTimestamp = new Date(0L);

    public MetricCopyExporter(String prefix, MetricRepository metricRepository) {
        this.prefix = StringUtils.isBlank(prefix) ? ""
                : (prefix.endsWith(".") ? prefix : prefix + ".");
        this.metricRepository = metricRepository;
    }

    public List<Metric<?>> export() {
        if (this.processing.compareAndSet(false, true)) {
            long latestTimestamp = System.currentTimeMillis();
            try {
                return doExport();
            } finally {
                this.latestTimestamp = new Date(latestTimestamp);
                this.processing.set(false);
            }
        }
        return Collections.emptyList();
    }

    private List<Metric<?>> doExport() {
        List<Metric<?>> metrics = new ArrayList<>();
        for (Metric<?> metric : this.metricRepository.findAll()) {
            Date timestamp = metric.getTimestamp();
            if (!canExportTimestamp(timestamp)) {
                continue;
            }
            if (metric.getName().startsWith("counter.")) {
                metrics.add(getPrefixedMetric(calculateDelta(metric)));
            } else {
                metrics.add(getPrefixedMetric(metric));
            }
        }
        return metrics;
    }

    private boolean canExportTimestamp(Date timestamp) {
        if (this.ignoreTimestamps) {
            return true;
        }
        if (this.sendLatest && this.latestTimestamp.after(timestamp)) {
            return false;
        }
        return true;
    }

    private Metric<?> getPrefixedMetric(Metric<?> metric) {
        String name = this.prefix + metric.getName();
        return new Metric<Number>(name, metric.getValue(), metric.getTimestamp());
    }

    private Delta<?> calculateDelta(Metric<?> value) {
        long delta = value.getValue().longValue();
        Long old = this.counts.replace(value.getName(), delta);
        if (old != null) {
            delta = delta - old;
        } else {
            this.counts.putIfAbsent(value.getName(), delta);
        }
        return new Delta<Long>(value.getName(), delta, value.getTimestamp());
    }

}
