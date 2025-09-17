package com.studentManagementSystem.Student.Management.System.controller;

import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/dashboard/{rollNumber}")
    public ResponseEntity<?> getDashboard(@PathVariable String rollNumber){
        StudentModel student = studentService.getSingleStudent(rollNumber);

        if(student == null){
            return ResponseEntity.notFound().build();
        }

        Map<String , Object> dashboard = new HashMap<>();
        dashboard.put("Profile" , student);
        dashboard.put("Notes" , student.getNotes());
        dashboard.put("Marks" , student.getMarks());

        return ResponseEntity.ok(dashboard);
    }
}
