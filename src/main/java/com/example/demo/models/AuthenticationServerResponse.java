package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public class AuthenticationServerResponse {
    @JsonProperty("status")
    private Boolean status = false;

    @JsonProperty("id")
    private String id;

    @JsonProperty("token")
    private String token = UUID.randomUUID().toString();

    @JsonProperty("token_name")
    private String token_name = "custom-auth-token";

    public AuthenticationServerResponse(String massage) {
        try {
            AuthenticationServerResponse response= (new ObjectMapper()).readValue(massage, AuthenticationServerResponse.class);
            this.status = response.getStatus();
            this.id = response.getId();
            this.token = response.getToken();
            this.token_name = response.getToken_name();
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
