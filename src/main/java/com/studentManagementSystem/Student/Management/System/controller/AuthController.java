package com.studentManagementSystem.Student.Management.System.controller;

import com.studentManagementSystem.Student.Management.System.service.CustomUserDetailsService;
import com.studentManagementSystem.Student.Management.System.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sms")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    private ResponseEntity<?> login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // We now pass the full userDetails object
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    @PostMapping("/teacher/login")
    public ResponseEntity<?> teacherLogin(@RequestParam String username, @RequestParam String password) {
        return login(username, password);
    }

    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestParam String rollNumber, @RequestParam String password) {
        return login(rollNumber, password);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestParam String username, @RequestParam String password) {
        return login(username, password);
    }
}

