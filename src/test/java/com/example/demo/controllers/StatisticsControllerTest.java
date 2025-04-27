package com.example.demo.controllers;

import org.example.controllers.StatisticsController;
import org.example.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsControllerTest {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPeers_ShouldReturnResponseEntity() {
        int page = 1;
        int size = 10;
        String campusId = "15";
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Peers List");

        when(statisticsService.peers(page, size, campusId)).thenReturn(mockResponse);


        ResponseEntity<String> response = statisticsController.getPeers(page, size, campusId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Peers List", response.getBody());
        verify(statisticsService, times(1)).peers(page, size, campusId);
    }
}
