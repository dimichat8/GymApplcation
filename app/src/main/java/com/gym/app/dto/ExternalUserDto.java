package com.gym.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExternalUserDto {
    private String email;
    private String password; // Adjust based on the actual roles structure
}