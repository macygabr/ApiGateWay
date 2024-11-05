package com.example.demo.service.kafka;

import com.example.demo.models.AuthenticationServerResponse;
import com.example.demo.service.authentication.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaConsumerService {
    private final AuthenticationService authService;

    public KafkaConsumerService(AuthenticationService authService) {
        this.authService = authService;
    }
    @KafkaListener(topics = "auth", groupId = "auth-service")
    public void consumeMessage(String message) throws JsonProcessingException {
        System.out.println("\033[31mMessage: <<" + message + ">> topics = auth \033[0m");
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationServerResponse response = mapper.readValue(message, AuthenticationServerResponse.class);
        CompletableFuture<String> futureResponse = authService.getPendingRequests().get(response.getId());

        futureResponse.complete(response.getToken());
    }
}
