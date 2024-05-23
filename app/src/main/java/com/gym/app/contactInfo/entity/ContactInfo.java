package com.gym.app.contactInfo.entity;

import com.gym.app.customer.entity.Customer;
import com.gym.app.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String email;
    private String mobilePhone;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id=" + id +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
