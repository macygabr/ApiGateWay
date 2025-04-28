package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.service.KafkaService;
import org.example.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "Статистика", description = "Получение статистики")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @CrossOrigin(origins = {"https://macygabr.github.io", "http://37.194.168.90:3002/"})
    @Operation(summary = "Получение списка пиров")
    @GetMapping("/peers")
    public ResponseEntity<String> getPeers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "46e7d965-21e9-4936-bea9-f5ea0d1fddf2") String campusId) {
        return statisticsService.peers(page, size, campusId);
    }
}