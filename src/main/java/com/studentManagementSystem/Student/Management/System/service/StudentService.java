package com.studentManagementSystem.Student.Management.System.service;

import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private  StudentRepository repo;

//    //auto dependency injection
//    @Autowired
//    public StudentService (StudentRepository repo){
//        this.repo = repo;
//    }


    //get all students
    public List<StudentModel> getAllStudents(){
        return repo.findAll();
    }

    //get student

    //get single student
    public StudentModel getSingleStudent(String rollNumber){
      return repo.findByRollNumber(rollNumber).orElse(null);
    }


    //add a student
    public StudentModel addStudent(StudentModel student){
        student.setRole("STUDENT");
        return repo.save(student);
    }

    //add Notes for student
    public void addNotes(String rollNumber , String notes){
        StudentModel student = getSingleStudent(rollNumber);
        if(student != null){
            student.setNotes(notes);
            repo.save(student);
        }
    }

    //update marks for subject
    public void updateMarks(String rollNumber , String subject  , int marks){
        StudentModel student = getSingleStudent(rollNumber);
        if(student != null){
            student.getMarks().put(subject , marks);
            repo.save(student);
        }
    }


}
