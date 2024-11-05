package com.example.demo.service.authentication;

import com.example.demo.models.SignInRequest;
import com.example.demo.models.SignUpRequest;
import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


@Getter
@Service
public class AuthenticationService {

    private final KafkaProducerService kafkaProducer;


    @Autowired
    public AuthenticationService(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void signIn(SignInRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);
        kafkaProducer.sendMessage("auth", json);
    }

    public void signUp(SignUpRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);
        kafkaProducer.sendMessage("auth", json);
    }

    public void logout(){
        kafkaProducer.sendMessage("auth", "Logout");
    }
}
