package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.KafkaService;
import org.example.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaService kafkaService;

    @InjectMocks
    private StatisticsService statisticsService;

    private final String topic = "peers";

    @BeforeEach
    void setUp() {
        Mockito.reset(objectMapper, kafkaService);
    }

    @Test
    void peers_ShouldReturnResponseEntity() throws JsonProcessingException {

    }

    @Test
    void peers_ShouldThrowException_WhenJsonProcessingFails() throws JsonProcessingException {

    }
}
