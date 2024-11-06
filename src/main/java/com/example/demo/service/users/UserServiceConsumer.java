package com.example.demo.service.users;


import com.example.demo.models.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;


@Service
public class UserServiceConsumer {

    private final UserServiceProducer userService;

    public UserServiceConsumer(UserServiceProducer userService) {
        this.userService = userService;
    }
    @KafkaListener(topics = "user_response", groupId = "user_service")
    public void consumeMessage(String message) throws JsonProcessingException {

        UserResponse response = (new ObjectMapper()).readValue(message, UserResponse.class);
        CompletableFuture<String> futureResponse = userService.getPendingRequests().get(response.getId());

        String json = (new ObjectMapper()).writeValueAsString(response);
        if (futureResponse != null) {
            futureResponse.complete(json);
        } else {
            System.err.println("No matching request found for response: " + response);
        }
    }
}
