package com.studentManagementSystem.Student.Management.System.controller;

import com.studentManagementSystem.Student.Management.System.dto.NotesRequest;
import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import com.studentManagementSystem.Student.Management.System.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sms/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/dashboard/{username}")
    public ResponseEntity<?> getDashboard(@PathVariable String username) {
        TeacherModel teacher = teacherService.getProfile(username);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }

        List<StudentModel> students = teacherService.getAssignedStudents(teacher.getId());
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("Profile", teacher);
        dashboard.put("Students", students);

        return ResponseEntity.ok(dashboard);
    }

    // --- REVISED METHOD WITH TRY-CATCH FOR SPECIFIC ERRORS ---
    @PostMapping("/notes")
    public ResponseEntity<String> giveNotes(@RequestBody NotesRequest notesRequest) {
        try {
            teacherService.giveNotes(
                    notesRequest.getTeacherUsername(),
                    notesRequest.getRollNumber(),
                    notesRequest.getNotes()
            );
            return ResponseEntity.ok("Notes updated successfully.");
        } catch (RuntimeException e) {
            // Send the specific error message from the service back to the client
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/marks")
    public ResponseEntity<String> updateMarks(@RequestParam String teacherUsername , @RequestParam String rollNumber , @RequestParam int marks){
        boolean success = teacherService.updateMarks(teacherUsername, rollNumber, marks);

        if (success) {
            return ResponseEntity.ok("Marks updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Cannot update marks. Either teacher or student not found, or student is not assigned to this teacher.");
        }
    }
}

