package com.example.demo.service.users;

import com.example.demo.models.HttpException;
import com.example.demo.models.UserInfoResponse;
import com.example.demo.models.signIn.SignInResponse;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
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
        UserRequest userRequest = new UserRequest(authorizationHeader);

        try {
            System.err.println("user_info Request: " + userRequest);
            kafkaProducer.sendMessage("user_info", id, userRequest.toString());
            String responseMessage = pendingRequests.get(id).get(10, TimeUnit.SECONDS);
            System.err.println("user_info Response: " + responseMessage);
            UserInfoResponse response = new UserInfoResponse(responseMessage);

            if(response.getStatus() != HttpStatus.OK) {
                throw new HttpException(response.getStatus(), response.getMessage());
            }

            return ResponseEntity.status(response.getStatus()).body(response.toJson());
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth user", e);
        } finally {
            pendingRequests.remove(id);
        }
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserRequest {

        private String authorizationHeader;
        public UserRequest(String authorizationHeader){
            this.authorizationHeader = authorizationHeader;
        }

        @Override
        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert to JSON string", e);
            }
        }
    }
}
