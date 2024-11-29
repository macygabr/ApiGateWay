package com.example.demo.service.users;

import com.example.demo.models.HttpException;
import com.example.demo.models.InfoResponse;
import com.example.demo.models.FilterAuthorizationRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<String> getInfo(String id, String authorizationHeader) throws JsonProcessingException {
        FilterAuthorizationRequest filterAuthorizationRequest = new FilterAuthorizationRequest(authorizationHeader);

        try {
            System.err.println("user_info Request: " + filterAuthorizationRequest);
            kafkaProducer.sendMessage("user_info", id, filterAuthorizationRequest.toString());
            String responseMessage = pendingRequests.get(id).get(10, TimeUnit.SECONDS);
            System.err.println("user_info Response: " + responseMessage);
            InfoResponse response = new InfoResponse(responseMessage);

            if(response.getStatus() != HttpStatus.OK) {
                throw new HttpException(response.getStatus(), response.getMessage());
            }

            return ResponseEntity.status(response.getStatus()).body(responseMessage);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth user", e);
        } finally {
            pendingRequests.remove(id);
        }
    }
}
