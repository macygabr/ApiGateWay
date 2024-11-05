package com.example.demo.service.users;

import com.example.demo.models.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    private final KafkaProducerService kafkaProducer;

    @Autowired
    public UserService(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }


    public void getInfo() {
        kafkaProducer.sendMessage("user", "?");
    }
}
