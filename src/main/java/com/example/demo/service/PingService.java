package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PingService {
    private final KafkaService kafkaService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Logger logger = LoggerFactory.getLogger(PingService.class);

    @EventListener(ApplicationReadyEvent.class)
    public void startPing() {
        scheduler.scheduleAtFixedRate(
                this::ping,
                0,
                1,
                TimeUnit.MINUTES
        );
    }

    private void ping() {
        try {
            kafkaService.sendRequest("ping");
            logger.debug("Проверка соединения");
        } catch (Exception e) {
            logger.error("Ошибка при проверке соединения: {}", e.getMessage());
        }
    }
}