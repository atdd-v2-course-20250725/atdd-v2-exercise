package com.odde.atddv2;

import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import static com.github.leeonky.dal.Assertions.expect;

@Slf4j
public class KafkaSteps {

    @Autowired
    private TestKafkaConsumer kafkaConsumer;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @假如("清空队列{string}")
    public void 清空队列(String topic) {
        kafkaConsumer.clear(topic);
    }

    @那么("队列应为:")
    public void 队列应为(String expression) {
        expect(kafkaConsumer).should(expression);
    }

    @当("向队列{string}写入:")
    public void 向队列写入(String topic, String text) {
        kafkaTemplate.send(topic, text);
    }
}
