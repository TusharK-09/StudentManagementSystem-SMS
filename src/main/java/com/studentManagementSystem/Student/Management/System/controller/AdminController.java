package com.studentManagementSystem.Student.Management.System.controller;


import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import com.studentManagementSystem.Student.Management.System.repository.StudentRepository;
import com.studentManagementSystem.Student.Management.System.repository.TeacherRepository;
import com.studentManagementSystem.Student.Management.System.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    // dummy admin data
    private final String ADMIN_USERNAME = "ADMIN_USER";
    private final String ADMIN_PASSWORD = "admin@123";

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestParam  String username ,@RequestParam String password){
        if(username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)){
            return ResponseEntity.ok("Admin Logged In");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    //create a  teacher by admin
    @PostMapping("/teacher")
    public ResponseEntity<TeacherModel> createTeacher(@RequestBody TeacherModel teacher){
        return ResponseEntity.ok(teacherRepo.save(teacher));
    }


    //create student by admin
    @PostMapping("/student")
    public ResponseEntity<StudentModel> createStudent(@RequestBody StudentModel student){
        return ResponseEntity.ok(studentRepo.save(student));
    }

    //see all teacher
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherModel>> getAllTeachers(){
        return ResponseEntity.ok(teacherRepo.findAll());
    }

    //see all students
    @GetMapping("/students")
    public ResponseEntity<List<StudentModel>> getAllStudents(){
        return ResponseEntity.ok(studentRepo.findAll());
    }

    //assigning student to a teacher
    @PutMapping("/assign/{teacherUsername}/{studentRoll}")
    public ResponseEntity<?> assignStudentToTeacher(@PathVariable String teacherUsername,
                                                    @PathVariable String studentRoll) {
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername).orElse(null);
        StudentModel student = studentRepo.findByRollNumber(studentRoll).orElse(null);

        if (teacher == null || student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Teacher or Student not found");
        }

        teacher.getStudentRollNumbers().add(studentRoll);
        TeacherModel updated = teacherRepo.save(teacher);

        return ResponseEntity.ok(updated);
    }

    //admin update student details
    @PutMapping("/student/{studentRollNumber}")
    public ResponseEntity<?> updateStudent(@PathVariable String studentRollNumber , @RequestBody StudentModel updatedStudent){
        StudentModel student = studentRepo.findByRollNumber(studentRollNumber).orElse(null);
        if(student == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
        }

        student.setUsername(updatedStudent.getUsername());
        student.setPassword(updatedStudent.getPassword());
        student.setCourse(updatedStudent.getCourse());
        student.setBatch(updatedStudent.getBatch());
        student.setCgpa(updatedStudent.getCgpa());
        student.setNotes(updatedStudent.getNotes());
        student.setMarks(updatedStudent.getMarks());

        StudentModel saved  = studentRepo.save(student);
        return ResponseEntity.ok(saved);
    }

    //admin update teacher
    @PutMapping("/teacher/{teacherUsername}")
    public ResponseEntity<?> updateTeacher(@PathVariable String teacherUsername , @RequestBody TeacherModel updatedTeacher){
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername).orElse(null);

        if(teacher == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
        }
        teacher.setPassword(updatedTeacher.getPassword());
        teacher.setOffice(updatedTeacher.getOffice());
        teacher.setPosition(updatedTeacher.getPosition());
        teacher.setSubject(updatedTeacher.getSubject());
        teacher.setStudentRollNumbers(updatedTeacher.getStudentRollNumbers());

        TeacherModel updated = teacherRepo.save(teacher);
        return ResponseEntity.ok(updated);
    }
}
