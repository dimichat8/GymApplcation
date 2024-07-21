package com.gym.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutUserDto {

    private Long customerId;
    private List<WorkoutDto> workouts;
}
