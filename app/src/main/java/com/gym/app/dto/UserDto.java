package com.gym.app.dto;

import com.gym.app.contactInfo.ContactInfo;
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
    private String email;
    private String password;
    private List<ContactInfo> contactInfoDtos;
    private boolean isEnabled;
    private Role role;
    private List<Customer> customerList;
}
