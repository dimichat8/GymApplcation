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
    private String gender;
    private Integer age;
    private Long user_id;

}
