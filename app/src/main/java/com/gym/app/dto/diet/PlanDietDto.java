package com.gym.app.dto.diet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDietDto {
    private int id;
    private String name;
    private String duration;
    private LocalDate startDate;
    private LocalDate endDate;

    List<MealDietDto> mealDTOS = new ArrayList<>();
    List<FoodDietDto> foodDTOS = new ArrayList<>();
    List<CustomerDietDto> customerDTOS = new ArrayList<>();
}
