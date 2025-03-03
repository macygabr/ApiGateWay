package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final Logger logger = LoggerFactory.getLogger(StatisticsService.class);
    private final ObjectMapper objectMapper;
    private final KafkaService kafkaService;

    public ResponseEntity<String> peers(int page, int size, String campusId) {
        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("page", page);
            jsonMap.put("size", size);
            jsonMap.put("campusId", campusId);

            String json = objectMapper.writeValueAsString(jsonMap);
            logger.debug("Generated JSON: {}", json);
            String response = kafkaService.sendRequest("peers", json);
            logger.debug("Received response: {}", response);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            logger.error("Error creating JSON", e);
            throw new RuntimeException("Error creating JSON", e);
        }
    }
}
