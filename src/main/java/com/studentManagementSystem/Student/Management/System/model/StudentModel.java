package com.studentManagementSystem.Student.Management.System.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Student-Data")
public class StudentModel {

    @Id
    private String id;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be atleast 6 characters long")
    private String password;


    @NotBlank(message =  "Roll Number cannot be empty")
    @Indexed(unique = true)
    private String rollNumber;
    private String role = "STUDENT";  // STUDENT role is fixed

    private String notes;  //students can have any sample notes


    @NotBlank(message = "Course cannot be empty")
    private String course;

    @Min(value = 2000, message = "Batch year must be >= 2000")
    @Max(value = 2030, message = "Batch year must be <= 2030")
    private int batch;

    @DecimalMin(value = "0.0", inclusive = true, message = "Cgpa must be greater than 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Cgpa must be less than 10.0")
    private float cgpa;

   //marks stored in key value pair (apc = 32);
    private Map<String, Integer> marks = new HashMap<>();

}