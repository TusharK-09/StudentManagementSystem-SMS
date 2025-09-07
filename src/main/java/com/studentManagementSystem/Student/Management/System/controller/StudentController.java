package com.studentManagementSystem.Student.Management.System.controller;

import com.studentManagementSystem.Student.Management.System.model.User;
import com.studentManagementSystem.Student.Management.System.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final UserService userService;

    //dependency Injection
    public StudentController(UserService userService){
        this.userService = userService;
    }

    //Student get its username
    @GetMapping("/{username}")
    public User getStudent(@PathVariable String username){
        return userService.getStudent(username);
    }
}
