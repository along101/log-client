package com.along101.logclient.metrics;


import java.util.concurrent.ConcurrentHashMap;

public class LegacyMetricService implements MetricService {
    private InMemoryMetricRepository inMemoryMetricRepository;

    private final ConcurrentHashMap<String, String> names = new ConcurrentHashMap<String, String>();

    public LegacyMetricService() {
        this.inMemoryMetricRepository = new InMemoryMetricRepository();
    }

    @Override
    public void submit(String name, double value) {
        inMemoryMetricRepository.set(new Metric<Double>(wrapGauge(name), value));
    }

    @Override
    public void increment(String name, long value) {
        inMemoryMetricRepository.increment(new Delta<Long>(wrapCounter(name), value));
    }

    @Override
    public void decrement(String name, long value) {
        inMemoryMetricRepository.increment(new Delta<Long>(wrapCounter(name), 0 - value));
    }

    @Override
    public void reset(String metricName) {
        inMemoryMetricRepository.reset(wrapCounter(metricName));
    }

    @Override
    public MetricRepository getMetricRepository() {
        return this.inMemoryMetricRepository;
    }

    private String wrapGauge(String metricName) {
        String cached = this.names.get(metricName);
        if (cached != null) {
            return cached;
        }
        if (metricName.startsWith("gauge.")) {
            return metricName;
        }
        String name = "gauge." + metricName;
        this.names.put(metricName, name);
        return name;
    }

    private String wrapCounter(String metricName) {
        String cached = this.names.get(metricName);
        if (cached != null) {
            return cached;
        }
        if (metricName.startsWith("counter.")) {
            return metricName;
        }
        String name = "counter." + metricName;
        this.names.put(metricName, name);
        return name;
    }
}
