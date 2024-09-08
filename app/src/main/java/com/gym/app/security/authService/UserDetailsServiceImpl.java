package com.gym.app.security.authService;

import com.gym.app.security.authentication.UserInfoDetailsService;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Optional<User> userInfo = Optional.ofNullable(userRepository.findUserByEmail(email));
            return userInfo.map(UserInfoDetailsService::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Ο χρήστης " + email + " δεν βρέθηκε"));
        } catch (UsernameNotFoundException ex) {
            log.error("An error occurred while loading user by username: {}", ex);
            throw new UsernameNotFoundException("Σφάλμα κατά τη φόρτωση του χρήστη με email: " + email);
        }
    }
}
