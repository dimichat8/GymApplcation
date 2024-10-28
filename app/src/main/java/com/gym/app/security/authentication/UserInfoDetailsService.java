package com.gym.app.security.authentication;

import com.gym.app.customer.entity.GymCustomer;
import com.gym.app.enums.Role;
import com.gym.app.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


    public class UserInfoDetailsService implements UserDetails {


        private String name;
        private String password;
        private Role role;
        private boolean isLoggedIn;
        private String jwt;
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();


        public UserInfoDetailsService(User user) {
            name=user.getContactInfo().getEmail();
            password=user.getPassword();
            role=user.getRole();
            this.isLoggedIn = false;
        }

        public UserInfoDetailsService(GymCustomer customer) {
            name=customer.getContactInfo().getEmail();
            password=customer.getPassword();
            role = Role.ATHLETE;
            this.isLoggedIn = false;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));
            this.isLoggedIn = true;
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return name;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return this.isLoggedIn;
        }

    }
