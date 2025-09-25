package com.studentManagementSystem.Student.Management.System.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Teacher-Data")
public class TeacherModel {

    @Id
    private String id;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password length must be at least 6 characters")
    private String password;

    @NotBlank(message = "Subject cannot be empty")
    private String subject;

    @NotBlank(message = "Position cannot be empty")
    private String position; // Corrected from 'Position' to 'position'

    private String office;

    //students list assigned to teacher
    private List<String> studentRollNumbers = new ArrayList<>();
}
