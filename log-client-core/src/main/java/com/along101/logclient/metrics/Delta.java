package com.along101.logclient.metrics;

import java.util.Date;

public class Delta<T extends Number> extends Metric<T> {

    public Delta(String name, T value, Date timestamp) {
        super(name, value, timestamp);
    }

    public Delta(String name, T value) {
        super(name, value);
    }

}
