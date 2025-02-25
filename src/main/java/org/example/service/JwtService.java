package org.example.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public String getUsername(JwtAuthenticationToken authToken) {
        return authToken.getName();
    }

    public List<String> getRoles(JwtAuthenticationToken authToken) {
        return authToken.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
