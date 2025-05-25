package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final ObjectMapper objectMapper;
    private final KafkaService kafkaService;
    private static final String topic = "peers";

    public ResponseEntity<String> peers(String json) {
        try {
            log.debug("Received JSON from frontend: {}", json);
            String response = kafkaService.sendRequest(topic, json);
            log.debug("Received response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error while handling peer request", e);
            throw new RuntimeException("Error while handling peer request", e);
        }
    }

    public ResponseEntity<String> campuses(String json) {
        try {
            log.debug("Received JSON from frontend: {}", json);
            String response = kafkaService.sendRequest(topic, json);
            log.debug("Received response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error while handling campus request", e);
            throw new RuntimeException("Error while handling campus request", e);
        }
    }

}
