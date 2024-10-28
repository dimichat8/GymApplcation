package com.gym.app.workout.entity;

import com.gym.app.customer.entity.GymCustomer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String duration;
    private String description;
    private String week;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private GymCustomer customer;
}
