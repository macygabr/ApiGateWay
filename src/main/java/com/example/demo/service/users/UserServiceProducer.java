package com.example.demo.service.users;

import com.example.demo.models.UserResponse;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;


@Service
@Getter
public class UserServiceProducer {

    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();
    private final KafkaProducerService kafkaProducer;

    @Autowired
    public UserServiceProducer(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }


    public String getInfo() throws JsonProcessingException {
        UserRequest userRequest = new UserRequest();
        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        pendingRequests.put(userRequest.getId(), futureResponse);
        kafkaProducer.sendMessage("user_info", userRequest.toString());

        try {
            String message = futureResponse.get(10, TimeUnit.SECONDS);
            System.err.println("user_info Response: " + message);
            UserResponse response = new UserResponse(message);
            if(!response.getStatus()) throw new RuntimeException(response.getMessage());
            return message;
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth user");
        } finally {
            pendingRequests.remove(userRequest.getId());
        }
    }

    @Data
    private static class UserRequest {

        private String id = UUID.randomUUID().toString();

        @Override
        public String toString() {
            String json;
            try {
                json = (new ObjectMapper()).writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return json;
        }
    }
}
