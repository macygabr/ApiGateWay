package org.example.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    public ResponseEntity<String> getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            String userId = jwt.getClaimAsString("sub"); // Получаем ID пользователя
            String username = jwt.getClaimAsString("preferred_username"); // Получаем имя
            String email = jwt.getClaimAsString("email"); // Получаем email

            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            List<String> roles = (List<String>) realmAccess.get("roles"); // Получаем роли

            return ResponseEntity.ok("hi " + username + " your role is " + roles);
        }
        return ResponseEntity.ok("hi anonymous");
    }
}
