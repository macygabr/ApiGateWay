package com.example.demo.service;

import com.example.demo.models.exception.CustomTimeoutException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;

    String sendRequest(String topic, String message){
        String id = String.valueOf(UUID.randomUUID());
        try {
            JsonNode jsonNode;

            if (message == null || message.isBlank()) {
                jsonNode = objectMapper.createObjectNode();
            } else {
                jsonNode = objectMapper.readTree(message);
            }

            if (jsonNode.isObject()) {
                ((ObjectNode) jsonNode).put("userId", userService.getCurrentUser().getId());
                message = jsonNode.toString();
            }

            kafkaTemplate.send(topic, id, message);
            tasks.put(id, new CompletableFuture<>());
            return tasks.get(id).get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new CustomTimeoutException("Время ожидания превышено");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Произошла ошибка: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            tasks.remove(id);
        }
    }

    String sendRequest(String topic){
        return sendRequest(topic, null);
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
