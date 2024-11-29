package com.example.demo.service.hhrecruter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class HHServiceConsumer {
    private final HHServiceProducer hhServiceProducer;

    public HHServiceConsumer(HHServiceProducer hhServiceProducer) {
        this.hhServiceProducer = hhServiceProducer;
    }
    @KafkaListener(topics = "response", groupId = "hh_service")
    public void consumeMessage(ConsumerRecord<String, String> message) {

        CompletableFuture<String> futureResponse = hhServiceProducer.getPendingRequests().get(message.key());
        if (futureResponse != null) {
            futureResponse.complete(message.value());
        } else {
            System.err.println("No matching request found: " + message.key());
        }
    }
}