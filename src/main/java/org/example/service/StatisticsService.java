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

    public ResponseEntity<String> peers(int page, int size, String campusId) {
        try {
            System.err.println(campusId);
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("page", page);
            jsonMap.put("size", size);
            jsonMap.put("campusId", campusId);

            String json = objectMapper.writeValueAsString(jsonMap);
            log.debug("Generated JSON: {}", json);
            String response = kafkaService.sendRequest(topic, json);
            log.debug("Received response: {}", response);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON", e);
            throw new RuntimeException("Error creating JSON", e);
        }
    }
}
