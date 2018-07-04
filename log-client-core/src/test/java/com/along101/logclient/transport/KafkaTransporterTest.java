package com.along101.logclient.transport;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/8/25.
 */
public class KafkaTransporterTest {
    @Test
    public void test() throws Exception {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put("bootstrap.servers","localhost:9092");
        LazyProducer lazyProducer = new LazyProducer(producerConfig);
        KafkaTransporter kafkaTransporter = new KafkaTransporter("framework.log", false, lazyProducer, null);
        kafkaTransporter.start();
        //发送测试
        kafkaTransporter.sendTest(10000, "456789");
    }
}
