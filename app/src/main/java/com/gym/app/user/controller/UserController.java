package com.gym.app.user.controller;

import com.gym.app.dto.AllDto;
import com.gym.app.dto.UserDto;
import com.gym.app.dto.UsernameDto;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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
        return userService.registerUser(userDto);
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

    @GetMapping("/get/byEmail")
    public ResponseEntity<String> getUserByEmail(@RequestBody UsernameDto usernameDto) {
        String username = userService.getUserByEmail(usernameDto.getUsername());
        return ResponseEntity.ok(username);
    }
}
