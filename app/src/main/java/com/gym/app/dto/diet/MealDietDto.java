package com.gym.app.dto.diet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDietDto {

        private int id;
        private String name;
        private String description;
        private String quantity;
        private String day;
        private String type;

}
