package com.along101.logclient.transport;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/6/16.
 */
public class LazyProducer {

    private volatile Producer<byte[], byte[]> producer;

    protected Map<String, Object> producerConfig;

    public LazyProducer(Map<String, Object> producerConfig) {
        this.producerConfig = getDefaultConfig();
        this.producerConfig.putAll(producerConfig);
    }

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("key.serializer", ByteArraySerializer.class.getName());
        config.put("value.serializer", ByteArraySerializer.class.getName());
        config.put("retries", "0");
        config.put("max.block.ms", "0");
        config.put("acks", "1");
        config.put("batch.size", "16384");
        config.put("linger.ms", "1");
        config.put("buffer.memory", "33554432");
        return config;
    }

    public Producer<byte[], byte[]> get() {
        Producer<byte[], byte[]> result = this.producer;
        if (result == null) {
            synchronized (this) {
                result = this.producer;
                if (result == null) {
                    this.producer = result = this.initialize();
                }
            }
        }
        return result;
    }

    protected Producer<byte[], byte[]> initialize() {
        Producer<byte[], byte[]> producer = new KafkaProducer<>(new HashMap<>(producerConfig));
        return producer;
    }

    public void destroy() {
        if (isInitialized()) {
            this.producer.flush();
            this.producer.close();
            this.producer = null;
        }
    }

    public boolean isInitialized() {
        return producer != null;
    }
}