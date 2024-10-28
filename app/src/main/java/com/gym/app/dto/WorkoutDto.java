package com.gym.app.dto;

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
    private String description;
    private String week;
    private Long customerId;
}
