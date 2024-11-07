package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @JsonProperty("id")
    private String id = "0";
    @JsonProperty("status")
    private Boolean status = false;
    @JsonProperty("firstname")
    private String firstname = "";
    @JsonProperty("lastname")
    private String lastname = "";
    @JsonProperty("message")
    private String message = "";
    public UserResponse(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            UserResponse response = new ObjectMapper().readValue(message, UserResponse.class);
            this.id = response.getId();
            this.firstname = response.getFirstname();
            this.lastname = response.getLastname();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON string", e);
        }
    }
}
