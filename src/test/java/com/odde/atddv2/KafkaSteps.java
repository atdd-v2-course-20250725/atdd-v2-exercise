package com.odde.atddv2;

import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.那么;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.UUID;

import static java.util.Collections.singletonList;

public class KafkaSteps {

    private KafkaConsumer<String, String> consumer;

    @假如("清空队列{string}")
    public void 清空队列(String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka.tool.net:9094");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(singletonList(topic));
    }

    @那么("队列{string}应为:")
    public void 队列应为(String arg0) {
    }
}
