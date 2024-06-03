package com.gym.app.dto;

import com.gym.app.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDto {

    private Long id;
    private String name;
    private String type;
    private String duration;
    private Long customerId;
}
