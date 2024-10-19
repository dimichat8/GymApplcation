package com.gym.app.user.service;

import com.gym.app.dto.AllDto;
import com.gym.app.dto.UserGymDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserGymDto> getUsers();

    Optional<UserGymDto> getUserById(Long id);

    ResponseEntity<String> registerUser(UserGymDto userDto);

    void updateUser(UserGymDto userDto, Long id);

    void deleteUser(Long id);

    AllDto all(String email);

    String getUserByEmail(String email);
}
