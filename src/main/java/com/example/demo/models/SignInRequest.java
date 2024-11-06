package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.UUID;

@Data
public class SignInRequest {
    @JsonProperty("id")
    private String id = UUID.randomUUID().toString();

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    public SignInRequest(String massage){
        try {
            SignInRequest signInRequest= (new ObjectMapper()).readValue(massage, SignInRequest.class);
            this.id = signInRequest.getPassword();
            this.email = signInRequest.getEmail();
            this.password = signInRequest.getPassword();
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    @Override
    public String toString() {
        String json = null;
        try {
            json = (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}

