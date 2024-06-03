package com.gym.app.user.controller;

import com.gym.app.dto.AllDto;
import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import com.gym.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userList = userService.getUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Optional<UserDto>> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        userService.updateUser(userDto, id);
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @GetMapping("/getAllActivities")
    public ResponseEntity<AllDto> getAllActivities() {
        AllDto allDto = userService.all();
        return ResponseEntity.ok(allDto);
    }
}
