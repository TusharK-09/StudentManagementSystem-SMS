package com.studentManagementSystem.Student.Management.System.dto;

public class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() { return username; } // return value by postman in json
    public void setUsername(String username) { this.username = username; }  // then set value to field
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
