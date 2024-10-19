package com.gym.app.dto.diet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDietDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String address;
    private LocalDate birthday;
    private String gender;
    private List<PlanDietDto> plans = new ArrayList<>();


    private UserDietDto userInfo;

    private List<CustomerInfoDietDto> customerInfos = new ArrayList<>();
}
