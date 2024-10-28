package com.gym.app.security.authService;

import com.gym.app.dto.AuthenticatedUserDto;
import com.gym.app.enums.Role;
import com.gym.app.security.authEntity.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final UserDetailsServiceImpl userDetailsService; // Primary app user details service
    private final ExternalAuthService externalAuthService; // External app user details service

    @Autowired
    public AuthenticationService(UserDetailsServiceImpl userDetailsService,
                                 ExternalAuthService externalAuthService) {
        this.userDetailsService = userDetailsService;
        this.externalAuthService = externalAuthService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(String email, String password) {
        // Attempt to authenticate in primary application
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails != null && validatePassword(password, userDetails.getPassword())) {
                // Return response with email and role
                return new AuthResponse(userDetails.getUsername(), userDetails.getAuthorities().stream()
                        .findFirst() // Get first role; assuming single role per user
                        .map(GrantedAuthority::getAuthority)
                        .orElse(Role.ATHLETE.toString()));
            }
        } catch (UsernameNotFoundException e) {
            log.info("User not found in primary app, checking external app...");
        }

        // Fallback to external authentication service
        AuthenticatedUserDto externalUserDetails = externalAuthService.authenticateExternalUser(email, password);
        if (externalUserDetails != null) {
            return new AuthResponse(externalUserDetails.getEmail(), externalUserDetails.getRole());
        }

        throw new BadCredentialsException("Authentication failed for user: " + email);
    }

    private boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}