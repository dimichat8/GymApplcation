package com.gym.app.dto;

import com.gym.app.customer.entity.Customer;
import com.gym.app.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String password;
    private ContactInfoDto contactInfoDto;
    private Boolean isLoggedIn;
    private Role role;
    private List<Customer> customerList;
}
