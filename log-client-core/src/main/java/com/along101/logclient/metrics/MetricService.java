package com.along101.logclient.metrics;

/**
 * Created by yinzuolong on 2017/4/6.
 */
public interface MetricService {

    void submit(String name, double value);

    void increment(String name, long value);

    void decrement(String name, long value);

    void reset(String metricName);

    MetricRepository getMetricRepository();
}
