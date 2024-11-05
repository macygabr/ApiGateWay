package com.example.demo.service.authentication;

import com.example.demo.models.AuthenticationServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void consumeMessage(String message) throws JsonProcessingException {
        System.out.println("\033[31mMessage: " + message +"\033[0m");
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationServerResponse response = mapper.readValue(message, AuthenticationServerResponse.class);
        CompletableFuture<String> futureResponse = authService.getPendingRequests().get(response.getId());

        if (futureResponse != null) {
            futureResponse.complete(response.getToken());
        } else {
            System.err.println("No matching request found for response: " + response);
        }
    }
}
