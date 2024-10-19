package com.gym.app.security.authController;

import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.ForgotPasswordRequest;
import com.gym.app.dto.SyncProfileDto;
import com.gym.app.dto.UserLoginDto;
import com.gym.app.enums.Role;
import com.gym.app.security.authentication.JwtHelper;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto) {
        String token = "";
        try {
            Authentication authentication = authenticate(loginDto.getEmail(), loginDto.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role;

            if (userDetails.getAuthorities() != null) {
                role = userDetails.getAuthorities().toString();
            } else {
                role = Role.ATHLETE.toString();
            }

             token = jwtHelper.generateToken(loginDto.getEmail(), role);
            /*String response = syncProfile(loginDto.getEmail(), loginDto.getPassword(), token);

            if (response != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }*/
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return token == null ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() : ResponseEntity.ok(token);
    }

    private Authentication authenticate(String email, String password) {
        System.out.println(email + "--------++--------" + password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        System.out.println("Sign in user details: " + userDetails);
        if (userDetails == null) {
            System.out.println("Sign in user details - null: " + userDetails);
            throw new BadCredentialsException("Invalid email or password");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            System.out.println("Sign in user details - wrong password" + userDetails);
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String syncProfile(String email, String password, String token) {
        String dietUrl = "http://localhost:8081/auth/authenticate";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        SyncProfileDto syncProfile = new SyncProfileDto();
        String response = "";
        try {
            syncProfile.setEmail(email);
            syncProfile.setPassword(password);
            syncProfile.setToken(token);
            response = restTemplate.postForObject(dietUrl, syncProfile, String.class);
        } catch (HttpClientErrorException ex) {
            String errorMessage = ex.getMessage();
            int statusCode = ex.getStatusCode().value();
            sendErrorToSecondApp(statusCode, errorMessage);
            ex.printStackTrace();
        }
        return response;
    }

    private void sendErrorToSecondApp(int statusCode, String errorMessage) {
        RestTemplate restTemplate = new RestTemplate();
        String errorUrl = "http://localhost:8081/auth/authenticate";

        Map<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("statusCode", statusCode);
        errorInfo.put("errorMessage", errorMessage);
        restTemplate.postForEntity(errorUrl, errorInfo, Void.class);
    }

    @PostMapping("/forgotPassword")
    private ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        boolean matchesUser = false;
        boolean matchesCustomer = false;
        User user = null;
        Customer customer = null;
        try {
            Optional<User> userOptional = userRepository.findUserByEmailAndPhone(forgotPasswordRequest.getEmail(), forgotPasswordRequest.getPhone());
            Optional<Customer> customerOptional = customerRepository.findCustomerByEmailAndPhone(forgotPasswordRequest.getEmail(), forgotPasswordRequest.getPhone());
            if (!userOptional.isPresent() && !customerOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Does not exist user!");
            }
            if (userOptional.isPresent()) {
                 user = userOptional.get();
                 matchesUser = forgotPasswordRequest.getEmail().equals(user.getContactInfo().getEmail()) && forgotPasswordRequest.getPhone().equals(user.getContactInfo().getMobilePhone());
            } else {
                 customer = customerOptional.get();
                 matchesCustomer = forgotPasswordRequest.getEmail().equals(customer.getContactInfo().getEmail()) && forgotPasswordRequest.getPhone().equals(customer.getContactInfo().getMobilePhone());
            }
            if (matchesUser) {
                user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
                userRepository.save(user);
            } else if (matchesCustomer) {
                customer.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
                customerRepository.save(customer);
            } else {
                return ResponseEntity.badRequest().body("Email or phone number does not match");
            }
            log.info("The new password is: {}", forgotPasswordRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

        return ResponseEntity.ok("Password has been updated successfully");
    }
}
