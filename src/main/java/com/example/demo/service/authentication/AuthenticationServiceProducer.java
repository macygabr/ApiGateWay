package com.example.demo.service.authentication;

import com.example.demo.models.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper mapper = new ObjectMapper();

        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        pendingRequests.put(request.getId(), futureResponse);

        String json = mapper.writeValueAsString(request);
        kafkaProducer.sendMessage("signin" ,json);

        try {
            return futureResponse.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth service");
        } finally {
            pendingRequests.remove(request.getId());
        }
    }

    public void signUp(SignUpRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);
        kafkaProducer.sendMessage("signup", json);
    }

    public void logout(){
        kafkaProducer.sendMessage("logout", "Logout");
    }


}
