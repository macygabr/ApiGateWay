package org.example.service;

import org.example.models.request.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class UserProfileService {
    private final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    public ResponseEntity<String> getUserProfile(){
        logger.debug("Request to get user profile");

        return ResponseEntity.ok("user profile...");
    }
}
