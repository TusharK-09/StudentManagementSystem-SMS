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

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudentService studentService;

    //teacher profile
    public TeacherModel getProfile(String username){
        return teacherRepo.findByUsername(username).orElse(null);
    }

    //load assigned students
    public List<StudentModel> getAssignedStudents(String teacherId){
        TeacherModel teacher = teacherRepo.findById(teacherId).orElse(null);
        if(teacher == null || teacher.getStudentRollNumbers() == null){
            return List.of();
        }
        List<StudentModel> students = new ArrayList<>();
        for (String roll : teacher.getStudentRollNumbers()) {
            studentRepo.findByRollNumber(roll).ifPresent(students::add);
        }
        return students;
    }

    // add notes
    public boolean giveNotes(String teacherUsername, String studentRollNumber, String notes) {
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername).orElse(null);
        if (teacher == null) {
            return false;
        }

        // check if the student is assigned to this teacher
        if (teacher.getStudentRollNumbers() != null && teacher.getStudentRollNumbers().contains(studentRollNumber)) {
            studentService.addNotes(studentRollNumber,notes);
            return true; //success on student assigned present in list
        }

        return false; // student not assigned to teacher
    }

    //update marks for corresponding subject
    public boolean updateMarks(String teacherUsername, String rollNumber, int marks) {
        TeacherModel teacher = teacherRepo.findByUsername(teacherUsername).orElse(null);
        StudentModel student = studentRepo.findByRollNumber(rollNumber).orElse(null);

        // check nulls
        if (teacher == null || student == null) {
            return false;
        }

        // check if teacher has assigned students list
        if (teacher.getStudentRollNumbers() == null ||
                !teacher.getStudentRollNumbers().contains(rollNumber)) {
            return false;
        }

        // make sure student marks map is initialized
        if (student.getMarks() == null) {
            student.setMarks(new HashMap<>());
        }

        // also check teacher has subject
        if (teacher.getSubject() == null) {
            return false;
        }

        // update marks for this teacher's subject
        student.getMarks().put(teacher.getSubject(), marks);
        studentRepo.save(student);

        return true;
    }


}
