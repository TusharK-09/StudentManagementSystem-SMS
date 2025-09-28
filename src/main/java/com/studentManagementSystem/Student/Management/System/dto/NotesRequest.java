package com.studentManagementSystem.Student.Management.System.dto;

import lombok.Data;

@Data
public class NotesRequest {
    private String teacherUsername; // Added field to identify the teacher
    private String rollNumber;      // Added field to identify the student
    private String notes;
}

