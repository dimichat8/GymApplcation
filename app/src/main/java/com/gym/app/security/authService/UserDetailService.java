package com.gym.app.security.authService;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailService {
    UserDetails loadUserByUsername(String email);
}
