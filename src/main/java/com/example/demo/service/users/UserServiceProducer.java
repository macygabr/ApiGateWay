package com.example.demo.service.users;

import com.example.demo.models.UserResponse;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<String> getInfo(String authorizationHeader) throws JsonProcessingException {
        UserRequest userRequest = new UserRequest(authorizationHeader);
        String id = UUID.randomUUID().toString();
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        pendingRequests.put(id, futureResponse);

        try {
            System.err.println("user_info Request: " + userRequest);
            String message = new ObjectMapper().writeValueAsString(userRequest);
            kafkaProducer.sendMessage("user_info", id, message);

            String responseMessage = futureResponse.get(10, TimeUnit.SECONDS);
            System.err.println("user_info Response: " + responseMessage);
            UserResponse userResponse = new UserResponse(responseMessage);
            return ResponseEntity.status(userResponse.getStatus()).body(userResponse.getMessage());
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth user", e);
        } finally {
            pendingRequests.remove(id);
        }
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserRequest {

        private String authorizationHeader;

        public UserRequest(String authorizationHeader){
            this.authorizationHeader = authorizationHeader;
        }
    }
}
