package com.gym.app.security.authController;

import com.gym.app.dto.ForgotPasswordRequest;
import com.gym.app.dto.UserLoginDto;
import com.gym.app.enums.Role;
import com.gym.app.security.authentication.JwtHelper;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto) {
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

            String token = jwtHelper.generateToken(loginDto.getEmail(), role);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @PostMapping("/forgotPassword")
    private ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            Optional<User> userOptional = userRepository.findUserByEmailAndPhone(forgotPasswordRequest.getEmail(), forgotPasswordRequest.getPhone());
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid email or phone number");
            }

            User user = userOptional.get();
            boolean matchesEmail = forgotPasswordRequest.getEmail().equals(user.getContactInfo().getEmail());
            boolean matchesPhone = forgotPasswordRequest.getPhone().equals(user.getContactInfo().getMobilePhone());

            if (matchesEmail && matchesPhone) {
                user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
                userRepository.save(user);
                log.info("The password is: {}", forgotPasswordRequest.getPassword());
            } else {
                return ResponseEntity.badRequest().body("Email or phone number does not match");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }

        return ResponseEntity.ok("Password has been updated successfully");
    }
}
