package com.studentManagementSystem.Student.Management.System.controller;


import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import com.studentManagementSystem.Student.Management.System.repository.StudentRepository;
import com.studentManagementSystem.Student.Management.System.repository.TeacherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData() {
        List<TeacherModel> teachers = teacherRepo.findAll();
        List<StudentModel> students = studentRepo.findAll();

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("teachers", teachers);
        dashboardData.put("students", students);

        return ResponseEntity.ok(dashboardData);
    }

    // --- NEW: ENDPOINT TO GET A SINGLE TEACHER'S DETAILS ---
    @GetMapping("/teacher/{username}")
    public ResponseEntity<TeacherModel> getTeacher(@PathVariable String username) {
        return teacherRepo.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- NEW: ENDPOINT TO GET A SINGLE STUDENT'S DETAILS ---
    @GetMapping("/student/{rollNumber}")
    public ResponseEntity<StudentModel> getStudent(@PathVariable String rollNumber) {
        return studentRepo.findByRollNumber(rollNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/teacher")
    public ResponseEntity<TeacherModel> createTeacher(@Valid @RequestBody TeacherModel teacher){
        if (teacher.getPassword() == null || teacher.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        return ResponseEntity.ok(teacherRepo.save(teacher));
    }


    @PostMapping("/student")
    public ResponseEntity<StudentModel> createStudent(@Valid @RequestBody StudentModel student){
        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return ResponseEntity.ok(studentRepo.save(student));
    }

    @PutMapping("/assign/{teacherUsername}/{studentRoll}")
    public ResponseEntity<?> assignStudentTo_Teacher(@PathVariable String teacherUsername,
                                                     @PathVariable String studentRoll) {
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername).orElse(null);
        StudentModel student = studentRepo.findByRollNumber(studentRoll).orElse(null);

        if (teacher == null || student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Teacher or Student not found");
        }

        if (!teacher.getStudentRollNumbers().contains(studentRoll)) {
            teacher.getStudentRollNumbers().add(studentRoll);
        }

        TeacherModel updated = teacherRepo.save(teacher);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/student/{studentRollNumber}")
    public ResponseEntity<?> updateStudent(@PathVariable String studentRollNumber , @RequestBody StudentModel updatedStudent){
        Optional<StudentModel> optionalStudent = studentRepo.findByRollNumber(studentRollNumber);
        if(optionalStudent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
        }

        StudentModel student = optionalStudent.get();
        student.setUsername(updatedStudent.getUsername());
        if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        }
        student.setCourse(updatedStudent.getCourse());
        student.setBatch(updatedStudent.getBatch());
        student.setCgpa(updatedStudent.getCgpa());
        student.setNotes(updatedStudent.getNotes());
        student.setMarks(updatedStudent.getMarks());

        StudentModel saved  = studentRepo.save(student);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/teacher/{teacherUsername}")
    public ResponseEntity<?> updateTeacher(@PathVariable String teacherUsername , @RequestBody TeacherModel updatedTeacher){
        Optional<TeacherModel> optionalTeacher = teacherRepo.findByUsername(teacherUsername);
        if(optionalTeacher.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
        }

        TeacherModel teacher = optionalTeacher.get();
        if (updatedTeacher.getPassword() != null && !updatedTeacher.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(updatedTeacher.getPassword()));
        }
        teacher.setOffice(updatedTeacher.getOffice());
        teacher.setPosition(updatedTeacher.getPosition());
        teacher.setSubject(updatedTeacher.getSubject());
        teacher.setStudentRollNumbers(updatedTeacher.getStudentRollNumbers());

        TeacherModel updated = teacherRepo.save(teacher);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/student/{studentRollNumber}")
    public ResponseEntity<?> deleteStudent(@PathVariable String studentRollNumber) {
        return studentRepo.findByRollNumber(studentRollNumber).map(student -> {
            studentRepo.delete(student);
            return ResponseEntity.ok("Student deleted successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found"));
    }

    @DeleteMapping("/teacher/{teacherUsername}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String teacherUsername) {
        return teacherRepo.findByUsername(teacherUsername).map(teacher -> {
            teacherRepo.delete(teacher);
            return ResponseEntity.ok("Teacher deleted successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found"));
    }
}

