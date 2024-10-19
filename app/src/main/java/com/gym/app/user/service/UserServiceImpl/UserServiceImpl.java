package com.gym.app.user.service.UserServiceImpl;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.contactInfo.repository.ContactInfoRepository;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.AllDto;
import com.gym.app.dto.UserGymDto;
import com.gym.app.enums.Role;
import com.gym.app.mapper.Map;
import com.gym.app.security.authController.AuthController;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.user.service.UserService;
import com.gym.app.workout.repository.WorkoutRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WorkoutRepository workoutRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
                user.setUserName("ADMIN");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRole(Role.ADMIN);
            userRepository.save(user);
            ContactInfo contactInfo = new ContactInfo();
                contactInfo.setPhone("6975885452");
                contactInfo.setEmail("admin@gmail.com");
                contactInfo.setUser(user);
                user.setContactInfo(contactInfo);
            contactInfoRepository.save(contactInfo);
            log.info("Create admin user");
        }
    }

    @Override
    public List<UserGymDto> getUsers() {
        return userRepository.findAll().stream()
                .map(Map::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserGymDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(Map::convertToUserDto);
    }
    @Transactional
    @Override
    public ResponseEntity<String> registerUser(UserGymDto userDto) {
        String email = userDto.getContactInfoDto().getEmail();
        Optional<User> existingUser = Optional.ofNullable(userRepository.findUserByEmail(email));
//        sendRegistrationToDietApp(userDto);
        if (existingUser.isPresent() ) {
            log.warn("User with email {} already exists", email);
            return ResponseEntity.ok("User is existed!");
        }
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setIsLoggedIn(userDto.getIsLoggedIn());
        user.setRole(Role.TRAINER);
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(userDto.getContactInfoDto().getPhone());
            contactInfo.setEmail(userDto.getContactInfoDto().getEmail());
            contactInfo.setMobilePhone(userDto.getContactInfoDto().getMobilePhone());
            contactInfo.setUser(savedUser);
        user.setContactInfo(contactInfo);
        contactInfoRepository.save(contactInfo);
        userRepository.save(user);
        user.setContactInfo(contactInfo);
        return ResponseEntity.ok("User registered successfully!");
    }

    private void sendRegistrationToDietApp(UserGymDto userDto) {
        RestTemplate restTemplate = new RestTemplate();
        String dietAppUrl = "http://localhost:8081/user/signUp";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<UserGymDto> requestEntity = new HttpEntity<>(userDto, headers);

        try {
            // Optionally include token handling if required
            // String token = retrieveToken(); // implement this to fetch the token if needed
            // headers.set("Authorization", "Bearer " + token);

            ResponseEntity<String> response = restTemplate.postForEntity(dietAppUrl, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully registered user in the diet app");
            } else {
                log.warn("Failed to register user in diet app: {}. Response body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error("Error occurred while sending registration to diet app: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Error occurred while communicating with the diet app: ", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending registration to diet app", e);
        }
    }

    @Override
    public void updateUser(UserGymDto userDto, Long id) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();
            user.setUserName(userDto.getUserName());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setIsLoggedIn(userDto.getIsLoggedIn());
            user.setRole(userDto.getRole());

            ContactInfo contactInfo = contactInfoRepository.findContactInfoByUser(existingUserOpt);
                        contactInfo.setPhone(userDto.getContactInfoDto().getPhone());
                        contactInfo.setEmail(userDto.getContactInfoDto().getEmail());
                        contactInfo.setMobilePhone(userDto.getContactInfoDto().getMobilePhone());
                        contactInfo.setUser(user);
                        contactInfoRepository.save(contactInfo);
            user.setContactInfo(contactInfo);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
           existingUser.ifPresent(user ->  userRepository.delete(user));
    }

    @Override
    public AllDto all(String email) {
        AllDto allDto = new AllDto();
       int countUsers = userRepository.countUsers();
       int countActiveCustomers = customerRepository.countActiveCustomers(email);
       int countDisabledCustomers = customerRepository.countDisabledCustomers(email);
       int countWorkouts = customerRepository.countWorkouts(email);
       allDto.setUsers(countUsers);
       allDto.setActiveCustomers(countActiveCustomers);
       allDto.setDisabledCustomers(countDisabledCustomers);
       allDto.setWorkouts(countWorkouts);
        System.out.println(allDto);
       return allDto;
    }

    @Override
    public String getUserByEmail(String email) {
        Optional<User> user = userRepository.findOptionalUserByEmail(email);
        UserGymDto userDto = Map.convertToUserDto(user.get());
        String username = userDto.getUserName();
        return username;
    }

}