package com.gym.app.user.service;

import com.gym.app.dto.AllDto;
import com.gym.app.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getUsers();

    Optional<UserDto> getUserById(Long id);

    ResponseEntity<String> registerUser(UserDto userDto);

    void updateUser(UserDto userDto, Long id);

    void deleteUser(Long id);

    AllDto all();

    String getUserByEmail(String email);
}
