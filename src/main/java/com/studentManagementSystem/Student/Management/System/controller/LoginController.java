package com.studentManagementSystem.Student.Management.System.controller;

import com.studentManagementSystem.Student.Management.System.dto.LoginRequest;
import com.studentManagementSystem.Student.Management.System.model.User;
import com.studentManagementSystem.Student.Management.System.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController //tells it's a REST API and will handle incoming traffic(http requests)
@RequestMapping("/api") // -> sets base url
public class LoginController {

    private final UserService userService;

    //dependency injection -> objects need to use other class object functions and are funded by external source
    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
