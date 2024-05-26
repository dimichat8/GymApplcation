package com.gym.app.dto;

import com.gym.app.customer.entity.Customer;
import com.gym.app.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDto {

    private Long id;
    private String phone;
    private String email;
    private String mobilePhone;
    private Long userId;
    private Long customerId;
}
