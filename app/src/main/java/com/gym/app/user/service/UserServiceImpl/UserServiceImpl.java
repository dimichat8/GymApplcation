package com.gym.app.user.service.UserServiceImpl;

import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(/*passwordEncoder.encode(*/userDto.getPassword());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }
}