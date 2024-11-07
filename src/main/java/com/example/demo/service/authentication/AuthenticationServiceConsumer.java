package com.example.demo.service.authentication;

import com.example.demo.models.AuthenticationServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    public void consumeMessage(ConsumerRecord<String, String> message) {

        CompletableFuture<String> futureResponse = authService.getPendingRequests().get(message.key());
        if (futureResponse != null) {
            futureResponse.complete(message.value());
        } else {
            System.err.println("No matching request found: " + message.key());
        }
    }
}
