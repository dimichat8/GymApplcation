package com.gym.app.security.authEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String email;
    private String role;
    private String token;

    public AuthResponse(String username, String string) {
    }
}