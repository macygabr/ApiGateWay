package com.example.demo.service.authentication;

import com.example.demo.models.AuthenticationServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CompletableFuture;

public class AuthenticationServiceConsumer {
    private final AuthenticationServiceProducer authService;

    public AuthenticationServiceConsumer(AuthenticationServiceProducer authService) {
        this.authService = authService;
    }
    @KafkaListener(topics = "auth/response", groupId = "auth/service")
    public void consumeMessage(String message) throws JsonProcessingException {
        System.out.println("\033[31mMessage: <<" + message + ">> topics = auth \033[0m");
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
