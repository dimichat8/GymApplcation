package com.gym.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.app.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;
    private String firstname;
    private String surname;
    private Integer age;
    private String gender;
    private Boolean isEnabled;
    private ContactInfoDto contactInfoDto;
    private Long userId;
}
