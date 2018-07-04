package com.along101.logclient.metrics;


/**
 * Convenient combination of reader and writer concerns.
 *
 * @author Dave Syer
 */
public interface MetricRepository {

    Metric<?> findOne(String metricName);

    Iterable<Metric<?>> findAll();

    long count();

    void set(Metric<?> value);

    void increment(Delta<?> delta);

    void reset(String metricName);

}
