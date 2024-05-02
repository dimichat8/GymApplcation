package com.gym.app.user.entity;

import com.gym.app.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String contactInfo;
    private boolean isEnabled;
    private boolean isLoggedIn;
    @Enumerated(EnumType.STRING)
    private Role role;
}