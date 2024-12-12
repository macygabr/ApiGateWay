package com.example.demo.service;

import com.example.demo.models.exception.CustomTimeoutException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ConcurrentHashMap<String, CompletableFuture<String>> tasks = new ConcurrentHashMap<>();

    String sendRequest(String topic, String message){
        String id = String.valueOf(UUID.randomUUID());
        try {
            System.err.println("sendRequest: " + topic);
            kafkaTemplate.send(topic, id, message);
            tasks.put(id, new CompletableFuture<>());
            return tasks.get(id).get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new CustomTimeoutException("Время ожидания превышено");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Произошла ошибка: " + e.getMessage());
        } finally {
            tasks.remove(id);
        }
    }

    @KafkaListener(topics = "response", groupId = "hh_service")
    private void consumeMessage(ConsumerRecord<String, String> message) {
        String id = message.key();
        String response = message.value();
        if (tasks.get(id) != null) {
            tasks.get(id).complete(response);
        }
    }
}
