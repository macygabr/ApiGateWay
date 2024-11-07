package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class AuthenticationServerResponse {

    @JsonProperty("id")
    private String id = "0";

    public AuthenticationServerResponse(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            AuthenticationServerResponse response = new ObjectMapper().readValue(message, AuthenticationServerResponse.class);
            this.id = response.getId();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }
}
