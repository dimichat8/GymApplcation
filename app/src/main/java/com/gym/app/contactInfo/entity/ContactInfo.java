package com.gym.app.contactInfo.entity;

import com.gym.app.customer.entity.GymCustomer;
import com.gym.app.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactInfoId;
    private String phone;
    private String email;
    private String mobilePhone;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private GymCustomer customer;

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id=" + contactInfoId +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
