package com.gym.app.dto.diet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoDietDto {

    private Long id;
    private LocalDateTime createdDate = LocalDateTime.now();
    private Integer age;
    private Double height;
    private Double water;
    private Double weight;
    private Double muscleMass;
    private Double bodyFatMass;
    private Double fat;
    @NumberFormat(pattern = "#,##0.0")
    private Double bmr;
    private Double tdee;
    private Integer activityLevel;
    private CustomerDietDto customer;
}
