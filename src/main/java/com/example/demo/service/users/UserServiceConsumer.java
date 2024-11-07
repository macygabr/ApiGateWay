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
    public void consumeMessage(String message) {
        UserResponse response = new UserResponse(message);
        CompletableFuture<String> futureResponse = userService.getPendingRequests().get(response.getId());

        try {
            futureResponse.complete(response.toString());
        } catch (Exception e){
            System.err.println("No matching request found for response: " + response);
        } finally {
            futureResponse.complete(response.toString());
        }
    }
}
