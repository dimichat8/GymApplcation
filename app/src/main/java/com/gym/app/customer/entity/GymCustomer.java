package com.gym.app.customer.entity;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.enums.Role;
import com.gym.app.user.entity.User;
import com.gym.app.workout.entity.Workout;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymCustomer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String surname;
    private String gender;
    private Integer age;
    private Boolean isEnabled;
    private String email;
    private String password;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;
    @Column(name = "profile_picture_name")
    private String profilePictureName;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ContactInfo contactInfo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Workout> workouts;
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Role getRole() {
        role = Role.ATHLETE;
        return role;
    }
}
