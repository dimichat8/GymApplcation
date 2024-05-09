package com.gym.app.user.service.UserServiceImpl;

import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(/*passwordEncoder.encode(*/userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setContactInfo(userDto.getContactInfoDtos());
        user.setEnabled(userDto.isEnabled());
        user.setRole(userDto.getRole());
        user.setCustomerList(userDto.getCustomerList());
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDto userDto, Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
                user.setName(userDto.getName());
                user.setPassword(/*passwordEncoder.encode(*/userDto.getPassword());
                user.setEmail(userDto.getEmail());
                user.setContactInfo(userDto.getContactInfoDtos());
                user.setEnabled(userDto.isEnabled());
                user.setRole(userDto.getRole());
                //user.setCustomerList(userDto.getCustomerList());
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id);}
    }
}