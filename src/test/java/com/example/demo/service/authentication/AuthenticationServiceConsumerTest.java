package com.example.demo.service.authentication;

import com.example.demo.models.AuthenticationServerResponse;
import com.example.demo.service.kafka.KafkaProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceConsumerTest {

    @Mock
    private AuthenticationServiceProducer authServiceProducer;

    @InjectMocks
    private AuthenticationServiceConsumer authServiceConsumer;

    private CompletableFuture<String> futureResponse;

    @BeforeEach
    void setUp() {
        futureResponse = new CompletableFuture<>();
    }

    @Test
    void testConsumeMessage_MatchingRequest() {

        String requestId = "testRequestId";
        String responseMessage = "{\"status\": 200, \"message\": \"Authenticated\"}";

        when(authServiceProducer.getPendingRequests()).thenReturn(new ConcurrentHashMap<>());
        authServiceProducer.getPendingRequests().put(requestId, futureResponse);

        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("auth_response", 0, 0, requestId, responseMessage);
        authServiceConsumer.consumeMessage(consumerRecord);

        assertTrue(futureResponse.isDone());
        assertEquals(responseMessage, futureResponse.join());
    }

    @Test
    void testConsumeMessage_NoMatchingRequest() {
        String requestId = "nonExistentRequestId";
        String responseMessage = "{\"status\": 200, \"message\": \"Authenticated\"}";

        when(authServiceProducer.getPendingRequests()).thenReturn(new ConcurrentHashMap<>());

        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("auth_response", 0, 0, requestId, responseMessage);
        authServiceConsumer.consumeMessage(consumerRecord);

        assertFalse(futureResponse.isDone(), "Future should not complete as there's no matching request");
    }
}
