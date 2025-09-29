package com.studentManagementSystem.Student.Management.System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean; // <-- IMPORTANT IMPORT
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void whenCreateStudent_withValidData_thenReturns200() throws Exception {
        // Arrange
        StudentModel newStudent = new StudentModel();
        newStudent.setRollNumber("2311993242");
        newStudent.setUsername("Akash Maneja");
        newStudent.setPassword("akash@123");
        newStudent.setCourse("Computer Engineering");
        newStudent.setBatch(2025);
        newStudent.setCgpa(9.1f);

        when(studentRepository.save(ArgumentMatchers.any(StudentModel.class))).thenReturn(newStudent);

        // Act & Assert
        mockMvc.perform(post("/admin/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rollNumber").value("2311993242"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void whenCreateStudent_withInvalidPassword_thenReturns400() throws Exception {
        // Arrange
        StudentModel invalidStudent = new StudentModel();
        invalidStudent.setRollNumber("222222222222");
        invalidStudent.setUsername("Ramesh");
        invalidStudent.setPassword("123"); // Invalid password
        invalidStudent.setCourse("ece");
        invalidStudent.setBatch(2024);
        invalidStudent.setCgpa(7.5f);


        mockMvc.perform(post("/admin/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());
    }
}
