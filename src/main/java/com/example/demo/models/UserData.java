package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;
    @JsonProperty("token_name")
    private String tokenName;

    @JsonProperty("firstname")
    private String firstname = "";
    @JsonProperty("lastname")
    private String lastname = "";

    public UserData(String message) {
        try {
            if (message == null) throw new RuntimeException("Message is null");
            UserData response = new ObjectMapper().readValue(message, UserData.class);
            this.status = response.getStatus();
            this.message = response.getMessage();
            this.token = response.getToken();
            this.tokenName = response.getTokenName();
            this.firstname = response.getFirstname();
            this.lastname = response.getLastname();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }
}