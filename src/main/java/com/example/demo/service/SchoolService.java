package com.example.demo.service;

import com.example.demo.models.s21.filter.FilterS21;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final KafkaService kafkaService;
    private final Logger logger = LoggerFactory.getLogger(SchoolService.class);
    private final ObjectMapper objectMapper;

    public String peers(int page, int size, String campusId, FilterS21 filter) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("page", page);
        jsonMap.put("size", size);
        jsonMap.put("campusId", campusId);
        jsonMap.put("filter", filter.toString());

        try {
            String json = objectMapper.writeValueAsString(jsonMap);
            logger.debug("Сформированный JSON: {}", json);

            return kafkaService.sendRequest("peers", json);
        } catch (JsonProcessingException e) {
            logger.error("Ошибка сериализации объекта в JSON", e);
            throw new RuntimeException("Не удалось создать JSON", e);
        }
    }
}
