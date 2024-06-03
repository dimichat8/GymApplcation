package com.gym.app.customer.entity;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.user.entity.User;
import com.gym.app.workout.entity.Workout;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
    private Boolean isEnabled;
    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ContactInfo contactInfo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Workout> workouts;
}
