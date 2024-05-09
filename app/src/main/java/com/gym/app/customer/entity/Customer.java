package com.gym.app.customer.entity;

import com.gym.app.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String surname;
    private String gender;
    private Integer age;

    //private List<ContactInfoDto> contactInfoDtoList = new ArrayList<>();
    private boolean isEnabled;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
