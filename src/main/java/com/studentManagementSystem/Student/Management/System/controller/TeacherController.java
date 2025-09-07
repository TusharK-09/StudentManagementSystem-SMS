package com.studentManagementSystem.Student.Management.System.controller;

import com.studentManagementSystem.Student.Management.System.dto.LoginRequest;
import com.studentManagementSystem.Student.Management.System.dto.NotesRequest;
import com.studentManagementSystem.Student.Management.System.model.User;
import com.studentManagementSystem.Student.Management.System.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final UserService userService;

    //dependency injection
    public TeacherController (UserService userService){
        this.userService =  userService;
    }

    //Get all students
    @GetMapping("/allStudents")
    public List<User> getStudents(){
        return userService.getStudents();
    }

    //Add Notes to a username
    @PostMapping("/addNotes")
    public String addNotes(@RequestBody NotesRequest notesRequest){
        userService.addNotes(notesRequest.getUsername() , notesRequest.getNotes());
        return "Notes added for : " + notesRequest.getUsername();
    }

    // Search student by username
    @GetMapping("/allStudents/{username}")
    public User searchStudent(@PathVariable String username) {
        for (User user : userService.getStudents()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null; // you can also throw custom exception later
    }

}
