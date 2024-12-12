package com.example.demo.service;

import com.example.demo.models.filter.Filter;
import com.example.demo.models.exception.CustomTimeoutException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class HHService {
    private final KafkaService kafkaService;

    public String profile(Long userId) {
        HhRegistry data = new HhRegistry(userId, "");
        return kafkaService.sendRequest("profile", data.toString());
    }

    public String getLink() {
        return kafkaService.sendRequest("getLink", "");
    }

    public String registry(Long userId, String code) {
        HhRegistry data = new HhRegistry(userId, code);
        return kafkaService.sendRequest("registry", data.toString());
    }

    public String start(Long userId) {
        HhRegistry data = new HhRegistry(userId, "");
        return kafkaService.sendRequest("start", data.toString());
    }

    public String stop() {
        return kafkaService.sendRequest("stop", "");
    }

    public String filter(Filter filter) {
        return kafkaService.sendRequest("filter", filter.toString());
    }

    @AllArgsConstructor
    private static class HhRegistry {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        @JsonProperty
        private Long userId;

        @JsonProperty
        private String code;

        @Override
        public String toString() {
            try {
                return OBJECT_MAPPER.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
