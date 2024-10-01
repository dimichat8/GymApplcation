package com.gym.app.dto;

public class UserLoginDto implements LoginDto {
    private String email;
    private String password;

    // Getters and setters
    @Override
    public String getEmail() { return email; }
    @Override
    public String getPassword() { return password; }

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLoginDto() {}

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
