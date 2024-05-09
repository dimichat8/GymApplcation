package com.gym.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDto {

    private String phone;
    private String email;
    private String mobilePhone;
}
