package com.example.demo.service;

import com.example.demo.models.Filter;
import com.example.demo.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class HHService {
    private CompletableFuture<String> task;
    private final KafkaProducerService kafkaProducer;

    public String getLink() {
        return sendRequest("getLink", "");
    }

    public String registry() {
        return sendRequest("registry", "");
    }

    public String start(Filter filter) {
        return sendRequest("start", filter.toString());
    }

    public String stop() {
        return sendRequest("stop", "");
    }

    private String sendRequest(String topic, String message){
        try {
            String id = String.valueOf(UUID.randomUUID());
            kafkaProducer.sendMessage(topic, id, message);
            task = new CompletableFuture<>();
            return task.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth user: " + e.getMessage());
        } finally {
            task = null;
        }
    }

    @KafkaListener(topics = "response", groupId = "hh_service")
    private void consumeMessage(ConsumerRecord<String, String> message) {
        if (task != null) {
            task.complete(message.value());
        } else {
            System.err.println("No active request found for response: " + message.key());
        }
    }
}
