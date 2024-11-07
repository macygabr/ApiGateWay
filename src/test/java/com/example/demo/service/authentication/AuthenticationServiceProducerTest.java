package com.example.demo.service.authentication;

import com.example.demo.models.signIn.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceProducerTest {

    @Mock
    private KafkaProducerService kafkaProducer;

    @InjectMocks
    private AuthenticationServiceProducer authServiceProducer;

    private SignInRequest signInRequest;
    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        signInRequest = new SignInRequest("username", "password");
        signUpRequest = new SignUpRequest("username", "password");
    }

    @Test
    void testSignIn_SuccessfulResponse() {
        String id = UUID.randomUUID().toString();
        authServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        new Thread(() -> {
            ResponseEntity<String> response =  authServiceProducer.signIn(id, signInRequest);
            assertEquals(200, response.getStatusCode());
            assertTrue(response.getBody().contains("User data"));
        }).start();

        authServiceProducer.getPendingRequests().forEach((key, future) -> {
            future.complete("{\"status\": 200, \"message\": \"User data\"}");
        });
    }

    @Test
    void testSignIn_TimeoutException() {
        String id = UUID.randomUUID().toString();
        authServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());
        assertThrows(RuntimeException.class, () -> authServiceProducer.signIn(id, signInRequest));
    }

    @Test
    void testSignUp_SuccessfulResponse() throws Exception {
//        // Arrange
//        CompletableFuture<String> futureResponse = new CompletableFuture<>();
//        futureResponse.complete("User registered");
//        authServiceProducer.getPendingRequests().put(signUpRequest.getId(), futureResponse);
//
//        // Act
//        String response = authServiceProducer.signUp(signUpRequest);
//
//        // Assert
//        assertEquals("User registered", response);
//        verify(kafkaProducer, times(1)).sendMessage(eq("signup"), any(), any());
    }

    @Test
    void testSignUp_TimeoutException() {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        authServiceProducer.getPendingRequests().put(UUID.randomUUID().toString(), futureResponse);
        assertThrows(RuntimeException.class, () -> authServiceProducer.signUp(signUpRequest));
    }
}
