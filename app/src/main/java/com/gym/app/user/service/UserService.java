package com.gym.app.user.service;

import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();

    Optional<User> getUserById(Long id);

    void registerUser(UserDto userDto);

    void updateUser(UserDto userDto, Long id);

    void deleteUser(Long id);
}
