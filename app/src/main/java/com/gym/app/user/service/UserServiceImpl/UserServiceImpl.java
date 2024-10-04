package com.gym.app.user.service.UserServiceImpl;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.contactInfo.repository.ContactInfoRepository;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.AllDto;
import com.gym.app.dto.UserDto;
import com.gym.app.enums.Role;
import com.gym.app.mapper.Map;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.user.service.UserService;
import com.gym.app.workout.repository.WorkoutRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(Map::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(Map::convertToUserDto);
    }
    @Transactional
    @Override
    public ResponseEntity<String> registerUser(UserDto userDto) {
        String email = userDto.getContactInfoDto().getEmail();
        Optional<User> existingUser = Optional.ofNullable(userRepository.findUserByEmail(email));
        if (existingUser.isPresent()) {
            log.warn("User with email {} already exists", email);
            return ResponseEntity.ok("User is existed!");
        }
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setIsLoggedIn(userDto.getIsLoggedIn());
        user.setRole(userDto.getRole());
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashedPassword);
        //user.setCustomerList(userDto.getCustomerList());

        User savedUser = userRepository.save(user);
        ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(userDto.getContactInfoDto().getPhone());
            contactInfo.setEmail(userDto.getContactInfoDto().getEmail());
            contactInfo.setMobilePhone(userDto.getContactInfoDto().getMobilePhone());
            contactInfo.setUser(savedUser);
        user.setContactInfo(contactInfo);
        contactInfoRepository.save(contactInfo);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @Override
    public void updateUser(UserDto userDto, Long id) {
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
    public AllDto all() {
        AllDto allDto = new AllDto();
       int countUsers = userRepository.countUsers();
       int countActiveCustomers = customerRepository.countActiveCustomers();
       int countDisabledCustomers = customerRepository.countDisabledCustomers();
       int countWorkouts = workoutRepository.countWorkouts();
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
        UserDto userDto = Map.convertToUserDto(user.get());
        String username = userDto.getUserName();
        return username;
    }

}