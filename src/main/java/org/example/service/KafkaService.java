package org.example.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final ConcurrentHashMap<String, CompletableFuture<String>> tasks = new ConcurrentHashMap<>();

    public String sendRequest(String topic, String message){
        logger.debug("Sending request to topic: {}", topic);
        String id = String.valueOf(UUID.randomUUID());
        try {
            kafkaTemplate.send(topic, id, message);
            logger.debug("Sent request with id: {}", id);
            tasks.put(id, new CompletableFuture<>());
            return tasks.get(id).get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT, "Timeout");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            tasks.remove(id);
        }
    }

    public String sendRequest(String topic){
        return sendRequest(topic, null);
    }

//    @KafkaListener(topics = "response", groupId = "api_service")
    private void consumeMessage(ConsumerRecord<String, String> message) {
//        String id = message.key();
//        String response = message.value();
//        if (tasks.get(id) != null) {
//            tasks.get(id).complete(response);
//        }
    }
}
