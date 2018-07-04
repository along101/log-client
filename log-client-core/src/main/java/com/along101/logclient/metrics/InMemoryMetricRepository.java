package com.along101.logclient.metrics;


import java.util.Date;
import java.util.concurrent.ConcurrentNavigableMap;
public class InMemoryMetricRepository implements MetricRepository {

    private final SimpleInMemoryRepository<Metric<?>> metrics = new SimpleInMemoryRepository<Metric<?>>();

    public void setValues(ConcurrentNavigableMap<String, Metric<?>> values) {
        this.metrics.setValues(values);
    }

    @Override
    public void increment(Delta<?> delta) {
        final String metricName = delta.getName();
        final int amount = delta.getValue().intValue();
        final Date timestamp = delta.getTimestamp();
        this.metrics.update(metricName, new SimpleInMemoryRepository.Callback<Metric<?>>() {

            @Override
            public Metric<?> modify(Metric<?> current) {
                if (current != null) {
                    return new Metric<Long>(metricName,
                            current.increment(amount).getValue(), timestamp);
                }
                return new Metric<Long>(metricName, (long) amount, timestamp);
            }

        });
    }

    @Override
    public void set(Metric<?> value) {
        this.metrics.set(value.getName(), value);
    }

    @Override
    public long count() {
        return this.metrics.count();
    }

    @Override
    public void reset(String metricName) {
        this.metrics.remove(metricName);
    }

    @Override
    public Metric<?> findOne(String metricName) {
        return this.metrics.findOne(metricName);
    }

    @Override
    public Iterable<Metric<?>> findAll() {
        return this.metrics.findAll();
    }

    public Iterable<Metric<?>> findAllWithPrefix(String prefix) {
        return this.metrics.findAllWithPrefix(prefix);
    }

}
