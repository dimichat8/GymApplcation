package com.gym.app.security.authService;

import com.gym.app.dto.AuthenticatedUserDto;
import com.gym.app.dto.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalAuthService {

    private final RestTemplate restTemplate;
    private final String externalAppUrl = "http://localhost:8081/auth/login";

    public ExternalAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthenticatedUserDto authenticateExternalUser(String email, String password) {
        String requestUrl = externalAppUrl;

        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        try {
            // Call the external application using POST
            ResponseEntity<AuthenticatedUserDto> response = restTemplate.postForEntity(requestUrl, loginDto, AuthenticatedUserDto.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Get the user details from the response body
            }
        } catch (HttpClientErrorException e) {
            log.error("Failed to fetch user from external app: {}", e.getMessage());
        }
        return null; // Return null if the external user is not found or another issue occurs
    }
}