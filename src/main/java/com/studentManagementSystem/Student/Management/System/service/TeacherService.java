package com.studentManagementSystem.Student.Management.System.service;

import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import com.studentManagementSystem.Student.Management.System.repository.StudentRepository;
import com.studentManagementSystem.Student.Management.System.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.studentManagementSystem.Student.Management.System.service.StudentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudentService studentService;

    public TeacherModel getProfile(String username){
        return teacherRepo.findByUsername(username).orElse(null);
    }

    public List<StudentModel> getAssignedStudents(String teacherId){
        TeacherModel teacher = teacherRepo.findById(teacherId).orElse(null);
        if(teacher == null || teacher.getStudentRollNumbers() == null){
            return List.of();
        }

        return teacher.getStudentRollNumbers().stream()
                .map(studentRepo::findByRollNumber)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList());
    }

    // --- REVISED METHOD WITH CLEARER ERROR HANDLING ---
    public void giveNotes(String teacherUsername, String studentRollNumber, String notes) {
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername)
                .orElseThrow(() -> new RuntimeException("Teacher not found with username: " + teacherUsername));

        if (teacher.getStudentRollNumbers() == null || !teacher.getStudentRollNumbers().contains(studentRollNumber)) {
            throw new RuntimeException("Forbidden: This student is not assigned to you.");
        }

        studentService.addNotes(studentRollNumber, notes);
    }

    public boolean updateMarks(String teacherUsername, String rollNumber, int marks) {
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername).orElse(null);
        StudentModel student = studentRepo.findByRollNumber(rollNumber).orElse(null);

        if (teacher == null || student == null) {
            return false;
        }

        if (teacher.getStudentRollNumbers() == null ||
                !teacher.getStudentRollNumbers().contains(rollNumber)) {
            return false;
        }

        if (student.getMarks() == null) {
            student.setMarks(new HashMap<>());
        }

        if (teacher.getSubject() == null) {
            return false;
        }

        student.getMarks().put(teacher.getSubject(), marks);
        studentRepo.save(student);

        return true;
    }
}
