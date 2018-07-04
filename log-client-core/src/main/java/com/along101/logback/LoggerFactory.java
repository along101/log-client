package com.along101.logback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class LoggerFactory {

    private static final Map<String, org.slf4j.Logger> loggerInstanceMap = new ConcurrentHashMap<>();

    public static synchronized org.slf4j.Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public static synchronized org.slf4j.Logger getLogger(String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }
}
