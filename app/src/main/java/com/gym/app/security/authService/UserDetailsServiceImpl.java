package com.gym.app.security.authService;

import com.gym.app.customer.entity.GymCustomer;
import com.gym.app.customer.repository.CustomerRepository;
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

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(email));
            Optional<GymCustomer> customer = Optional.ofNullable(customerRepository.findByEmail(email));
            if (user.isPresent()) {
                return user.map(UserInfoDetailsService::new)
                        .orElseThrow(() -> new UsernameNotFoundException("Ο χρήστης " + email + " δεν βρέθηκε"));
            } else if (customer.isPresent()){
                return customer.map(UserInfoDetailsService::new)
                        .orElseThrow(() -> new UsernameNotFoundException("Ο αθλητής " + email + " δεν βρέθηκε"));
            }
        } catch (UsernameNotFoundException ex) {
            log.error("An error occurred while loading user by username: {}", ex);
            throw new UsernameNotFoundException("Σφάλμα κατά τη φόρτωση του χρήστη με email: " + email);
        }
        return null;
    }
}
