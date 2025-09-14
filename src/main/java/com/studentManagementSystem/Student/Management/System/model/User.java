package com.studentManagementSystem.Student.Management.System.model;

public class User {
    public String username;
    public String password;
    public String role;  // STUDENT OR TEACHER
    public String notes;  //students can have any sample notes


    //attributes essentials for student
    public String course;
    public int batch;
    public float cgpa;

    //some teacher specific attributes
    public String position;
    public String office;
    public String subject;

    //constructor for username , password , role - > Student
    public User(String username, String password, String role , String course , int batch , float cgpa) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.course  = course;
        this.batch = batch;
        this.cgpa = cgpa;
    }

    //constructor for Teacher attributes -> have office , subject , position
    public User(String username, String password, String role , String subject , String position , String office ) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.subject  = subject;
        this.position = position;
        this.office = office;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public float getCgpa() {
        return cgpa;
    }

    public void setCgpa(float cgpa) {
        this.cgpa = cgpa;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
