package com.example.demo.controller;

import com.example.demo.service.hhrecruter.HHServiceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/hh/")
public class HHController {
    private final HHServiceProducer hhServiceProducer;

    @Autowired
    public HHController(HHServiceProducer hhServiceProducer) {
        this.hhServiceProducer = hhServiceProducer;
    }
    @GetMapping("/registry")
    public ResponseEntity<?> registry(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("registry");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.registry(id, authorizationHeader);
        System.err.println("hh registry: " + response);
        return response;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("callback");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.callback(id, authorizationHeader);
        System.err.println("hh callback: " + response);
        return response;
    }

    @GetMapping("/start")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("start");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.start(id, authorizationHeader);
        System.err.println("hh start: " + response);
        return response;
    }

    @GetMapping("/stop")
    public ResponseEntity<?> stop(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("stop");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.stop(id, authorizationHeader);
        System.err.println("hh stop: " + response);
        return response;
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(@RequestHeader("Authorization") String authorizationHeader) {
        System.err.println("status");

        String id = UUID.randomUUID().toString();
        hhServiceProducer.getPendingRequests().put(id, new CompletableFuture<>());

        ResponseEntity<String> response = hhServiceProducer.stop(id, authorizationHeader);
        System.err.println("hh status: " + response);
        return response;
    }
}