package com.example.demo.service.authentication;

import com.example.demo.models.AuthenticationServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationServiceConsumer {
    private final AuthenticationServiceProducer authService;

    public AuthenticationServiceConsumer(AuthenticationServiceProducer authService) {
        this.authService = authService;
    }
    @KafkaListener(topics = "auth_response", groupId = "auth_service")
    public void consumeMessage(String message) {

        AuthenticationServerResponse response = new AuthenticationServerResponse(message);
        CompletableFuture<String> futureResponse = authService.getPendingRequests().get(response.getId());

        if (futureResponse != null && response.getId() != null) {
            futureResponse.complete(message);
        } else {
            System.err.println("No matching request found for response: " + response);
        }
    }
}
