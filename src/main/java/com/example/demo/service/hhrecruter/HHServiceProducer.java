package com.example.demo.service.hhrecruter;

import com.example.demo.models.HttpException;
import com.example.demo.models.InfoResponse;
import com.example.demo.models.FilterAuthorizationRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.concurrent.*;

    @Service
    @Getter
    public class HHServiceProducer {
        private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();
        private final KafkaProducerService kafkaProducer;

        @Autowired
        public HHServiceProducer(KafkaProducerService kafkaProducer) {
            this.kafkaProducer = kafkaProducer;
        }

        public ResponseEntity<String> registry(String id, String authorizationHeader) {
            FilterAuthorizationRequest filterAuthorizationRequest = new FilterAuthorizationRequest(authorizationHeader);
            return sendRequest("hh_registry", id, filterAuthorizationRequest);
        }

        public ResponseEntity<String> callback(String id, String authorizationHeader) {
            FilterAuthorizationRequest filterAuthorizationRequest = new FilterAuthorizationRequest(authorizationHeader);
            return sendRequest("hh_callback", id, filterAuthorizationRequest);
        }

        public ResponseEntity<String> start(String id, String authorizationHeader) {
            FilterAuthorizationRequest filterAuthorizationRequest = new FilterAuthorizationRequest(authorizationHeader);
            return sendRequest("hh_start", id, filterAuthorizationRequest);
        }

        public ResponseEntity<String> stop(String id, String authorizationHeader) {
            FilterAuthorizationRequest filterAuthorizationRequest = new FilterAuthorizationRequest(authorizationHeader);
            return sendRequest("hh_stop", id, filterAuthorizationRequest);
        }


        private ResponseEntity<String> sendRequest(String topic,String id, FilterAuthorizationRequest filterAuthorizationRequest){
            try {
                System.err.println(topic+ " Request: " + filterAuthorizationRequest);
                kafkaProducer.sendMessage(topic, id, filterAuthorizationRequest.toString());
                String responseMessage = pendingRequests.get(id).get(10, TimeUnit.SECONDS);
                System.err.println("registry Response: " + responseMessage);
                InfoResponse response = new InfoResponse(responseMessage);
                if(response.getStatus() != HttpStatus.OK) {
                    throw new HttpException(response.getStatus(), response.getMessage());
                }
                return ResponseEntity.status(response.getStatus()).body(responseMessage);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "Timeout waiting for response from auth user: " + e.getMessage());
            } finally {
                pendingRequests.remove(id);
            }
        }
    }
