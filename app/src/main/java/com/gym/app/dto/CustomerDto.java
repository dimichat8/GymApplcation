package com.gym.app.dto;

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
    private byte[] profilePicture;
    private String profilePictureName;
    private ContactInfoDto contactInfoDto;
    private Long userId;
    private String password;
}
