package com.gym.app.dto;

import lombok.Data;

@Data
public class AuthenticatedUserDto {
    private String email;
    private String role; // Assuming a single role per user for simplicity
}