package com.example.demo.service.authentication;

import com.example.demo.models.HttpException;
import com.example.demo.models.signIn.SignInResponse;
import com.example.demo.models.signIn.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;
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

    public ResponseEntity<String> signIn(String id, SignInRequest request) {
        try {
            kafkaProducer.sendMessage("signin" ,id ,request.toString());
            String responseMessage = pendingRequests.get(id).get(10, TimeUnit.SECONDS);
            SignInResponse response = new SignInResponse(responseMessage);
            System.err.println("signin Response: " + response);

            if(response.getStatus() != HttpStatus.OK) {
                throw new HttpException(response.getStatus(), response.getMessage());
            }
            return ResponseEntity.status(response.getStatus()).body(response.toJson());
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR,"Timeout waiting for response from auth service");
        } finally {
            pendingRequests.remove(id);
        }
    }

    public String signUp(SignUpRequest request) {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        String id = UUID.randomUUID().toString();
        pendingRequests.put(id, futureResponse);

        try {
//            kafkaProducer.sendMessage("signup", request.toString());
            String res = futureResponse.get(10, TimeUnit.SECONDS);
            System.err.println("signUp Response: " + res);
            return res;
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Timeout waiting for response from auth service");
        } finally {
            pendingRequests.remove(id);
        }
    }

    public ResponseEntity<String> logout(String id, String token){
        try {
            kafkaProducer.sendMessage("logout", id, token);
            String responseMessage = pendingRequests.get(id).get(10, TimeUnit.SECONDS);
            SignInResponse response = new SignInResponse(responseMessage);

            if(response.getStatus() != HttpStatus.OK) {
                throw new HttpException(response.getStatus(), response.getMessage());
            }
            return ResponseEntity.status(response.getStatus()).body(response.toJson());

        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR,"Timeout waiting for response from auth service");
        } finally {
            pendingRequests.remove(id);
        }
    }
}
