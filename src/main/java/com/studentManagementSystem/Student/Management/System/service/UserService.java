package com.studentManagementSystem.Student.Management.System.service;

import com.studentManagementSystem.Student.Management.System.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();

    //default constructor
    public UserService(){
        // Preload  teacher user
        users.add(new User("Akhil Duneja", "akhil123", "TEACHER", "Computer Science", "Professor", "Room 301"));

        // Preload  student user
        users.add(new User("Rohit", "pass123", "STUDENT", "Btech CSE", 2025, 8.5f));
        users.add(new User("Aman", "pass234", "STUDENT", "Btech CSE", 2026, 7.8f));
        users.add(new User("Shruti", "pass345", "STUDENT", "BCA", 2025, 9.1f));
        users.add(new User("Tushar", "mysecret", "STUDENT", "MBA", 2024, 8.9f));
    }

    //authenticate function
    public User authenticate(String username , String password){
        for (User user : users){
            if(user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    //get all students
    public List<User> getStudents(){
            List<User> students = new ArrayList<>();
            for(User user : users){
                if(user.role.equals("STUDENT")){
                    students.add(user);
                }
            }
            return students;
    }

    //get a single user as Student
    public User getStudent(String username){
        for(User user :  users){
            if(user.username.equals(username) && user.role.equals("STUDENT")){
                return user;
            }
        }
        return null;
    }

    //add a student
    public void addStudent(User student){
        users.add(student);
    }

    //add notes for students
    public void addNotes(String username , String notes){
        User student = getStudent(username);
        if(student != null){
            student.notes = notes;
        }
    }

}

//sample commit