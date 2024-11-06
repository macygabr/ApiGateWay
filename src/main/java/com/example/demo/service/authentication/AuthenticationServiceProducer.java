package com.example.demo.service.authentication;

import com.example.demo.models.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.*;


@Getter
@Service
public class AuthenticationServiceProducer {
    private final KafkaProducerService kafkaProducer;
    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    @Autowired
    public AuthenticationServiceProducer(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public String signIn(SignInRequest request) throws JsonProcessingException {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        pendingRequests.put(request.getId(), futureResponse);
        kafkaProducer.sendMessage("signin" ,request.toString());

        try {
            return futureResponse.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth service");
        } finally {
            pendingRequests.remove(request.getId());
        }
    }

    public String signUp(SignUpRequest request) throws JsonProcessingException {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        pendingRequests.put(request.getId(), futureResponse);
        kafkaProducer.sendMessage("signup", request.toString());

        try {
            return futureResponse.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth service");
        } finally {
            pendingRequests.remove(request.getId());
        }
    }

    public void logout(){
        kafkaProducer.sendMessage("logout", "logout");
    }
}
