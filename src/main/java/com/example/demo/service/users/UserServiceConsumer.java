package com.example.demo.service.users;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    public void consumeMessage(ConsumerRecord<String, String> message) {
        CompletableFuture<String> futureResponse = userService.getPendingRequests().get(message.key());
        if (futureResponse != null) {
            futureResponse.complete(message.value());
        } else {
            System.err.println("No matching request found: " + message);
        }
    }
}
