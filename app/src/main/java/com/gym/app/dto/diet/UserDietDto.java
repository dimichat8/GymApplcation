package com.gym.app.dto.diet;

import com.gym.app.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDietDto {

    private int id;
    private String name;
    private String email;
    private String password;
    private String contactInfo;
    private boolean isLoggedIn;
    private Role role;
    private List<CustomerDietDto> customers = new ArrayList<>();
}
