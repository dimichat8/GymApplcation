package com.gym.app.dto.diet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDietDto {

        private int id;
        private String name;
        private String description;
        private double gram;
        private double calories;
        private String type;

}
