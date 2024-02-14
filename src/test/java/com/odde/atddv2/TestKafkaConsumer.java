package com.odde.atddv2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestKafkaConsumer {
    private final ConcurrentHashMap<String, Queue<String>> queue = new ConcurrentHashMap<>();

    @KafkaListener(topicPattern = ".*")
    public void listenToPatternTopics(ConsumerRecord<String, String> record) {
        queue.computeIfAbsent(record.topic(), k -> new ArrayDeque<>()).add(record.value());
    }

    public void clearAll() {
        queue.clear();
    }

    public void clear(String topic) {
        queue.remove(topic);
    }

    public ConcurrentHashMap<String, Queue<String>> getQueue() {
        return queue;
    }
}

