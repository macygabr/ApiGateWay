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
        // Arrange
        int page = 1;
        int size = 10;
        String campusId = "15";

        Map<String, Object> jsonMap = Map.of("page", page, "size", size, "campusId", campusId);
        String json = "{\"page\":1,\"size\":10,\"campusId\":\"15\"}";
        String mockResponse = "Kafka Response";

        when(objectMapper.writeValueAsString(jsonMap)).thenReturn(json);
        when(kafkaService.sendRequest(topic, json)).thenReturn(mockResponse);

        // Act
        ResponseEntity<String> response = statisticsService.peers(page, size, campusId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());

        // Verify that methods were called with expected arguments
        verify(objectMapper, times(1)).writeValueAsString(jsonMap);
        verify(kafkaService, times(1)).sendRequest(topic, json);
    }

    @Test
    void peers_ShouldThrowException_WhenJsonProcessingFails() throws JsonProcessingException {
        // Arrange
        int page = 1;
        int size = 10;
        String campusId = "15";

        Map<String, Object> jsonMap = Map.of("page", page, "size", size, "campusId", campusId);

        when(objectMapper.writeValueAsString(jsonMap)).thenThrow(new JsonProcessingException("Test Exception") {});

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> statisticsService.peers(page, size, campusId));
        assertEquals("Error creating JSON", thrown.getMessage());

        // Verify that ObjectMapper was called but KafkaService was NOT called
        verify(objectMapper, times(1)).writeValueAsString(jsonMap);
        verify(kafkaService, never()).sendRequest(any(), any());
    }
}
