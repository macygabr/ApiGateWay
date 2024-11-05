package com.example.demo.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "auth", groupId = "auth-service")
    public void consumeMessage(String message) {
        System.out.println("\033[31mMessage: <<" + message + ">> topics = auth \033[0m");
    }
}
