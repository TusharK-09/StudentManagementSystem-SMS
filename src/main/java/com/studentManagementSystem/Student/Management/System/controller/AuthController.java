package com.studentManagementSystem.Student.Management.System.controller;
import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import com.studentManagementSystem.Student.Management.System.service.StudentService;
import com.studentManagementSystem.Student.Management.System.service.TeacherService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController //tells it's a REST API and will handle incoming traffic(http requests)
@RequestMapping("/sms") // -> sets base url
public class AuthController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;


    //teacher auth validation
    @PostMapping("/teacher/login")
    public ResponseEntity<?> teacherLogin(@RequestParam String username , @RequestParam String password){
        TeacherModel teacher = teacherService.getProfile(username);
        if(teacher != null && teacher.getPassword().equals(password)){
            return ResponseEntity.ok(teacher);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    //student auth validation
    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestParam String rollNumber , @RequestParam String password){
        StudentModel student = studentService.getSingleStudent(rollNumber);
        if(student != null && student.getPassword().equals(password)){
            return ResponseEntity.ok(student);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }
}
