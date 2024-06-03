package com.gym.app.dto;

import com.gym.app.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDto {

    private int users;
    private int activeCustomers;
    private int disabledCustomers;
    private int workouts;
}
