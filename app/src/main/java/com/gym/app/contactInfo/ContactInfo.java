package com.gym.app.contactInfo;

import com.gym.app.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String phone;
    private String email;
    private String mobilePhone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
