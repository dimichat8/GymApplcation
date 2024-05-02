package com.gym.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {

    private String name;
    private String email;
    private String password;
}
