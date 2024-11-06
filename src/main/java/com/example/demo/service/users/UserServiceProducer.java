package com.example.demo.service.users;

import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;


@Service
@Getter
public class UserServiceProducer {

    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();
    private final KafkaProducerService kafkaProducer;

    @Autowired
    public UserServiceProducer(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }


    public String getInfo() throws JsonProcessingException {

        String requestId = UUID.randomUUID().toString();

        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        pendingRequests.put(requestId, futureResponse);

        kafkaProducer.sendMessage("user_info", "get");

        try {
            return futureResponse.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth user");
        } finally {
            pendingRequests.remove(requestId);
        }
    }
}
