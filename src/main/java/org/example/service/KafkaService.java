package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

@EnableKafka
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ConcurrentHashMap<String, CompletableFuture<String>> tasks = new ConcurrentHashMap<>();

    @Value("${kafka.request.timeout:10}") // Таймаут в секундах
    private long requestTimeout;

    public String sendRequest(String topic, String message) {
        log.info("Sending request to topic {}...", topic);
        String id = UUID.randomUUID().toString();
        CompletableFuture<String> future = new CompletableFuture<>();
        tasks.put(id, future);

        kafkaTemplate.send(topic, id, Optional.ofNullable(message).orElse(""));

        log.info("Sent request with id: {}", id);

        try {
            return future.get(requestTimeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request with id {} timed out", id);
            tasks.remove(id);
            throw new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT, "Timeout");
        } catch (InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Error occurred while processing request with id {}", id);
            tasks.remove(id);
            throw new RuntimeException(e);
        } finally {
            tasks.remove(id);
        }
    }

    @KafkaListener(topics = "response", groupId = "api_service")
    private void consumeMessage(ConsumerRecord<String, String> message) {
        String id = message.key();
        String response = message.value();
        log.info("Received response for id: {}", id);

        CompletableFuture<String> future = tasks.remove(id);

        if (future != null) {
            future.complete(response);
        } else {
            log.warn("No pending task found for id: {}", id);
        }
    }
}
