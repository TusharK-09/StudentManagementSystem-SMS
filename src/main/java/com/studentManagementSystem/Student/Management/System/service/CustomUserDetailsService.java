package com.studentManagementSystem.Student.Management.System.service;

import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import com.studentManagementSystem.Student.Management.System.repository.StudentRepository;
import com.studentManagementSystem.Student.Management.System.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUsername)) {
            return User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .authorities("ROLE_ADMIN")
                    .build();
        }

        Optional<TeacherModel> teacher = teacherRepository.findByUsername(username);
        if (teacher.isPresent()) {
            TeacherModel teacherModel = teacher.get();
            return User.builder()
                    .username(teacherModel.getUsername())
                    .password(teacherModel.getPassword())
                    .authorities("ROLE_TEACHER")
                    .build();
        }

        Optional<StudentModel> student = studentRepository.findByRollNumber(username);
        if (student.isPresent()) {
            StudentModel studentModel = student.get();
            return User.builder()
                    .username(studentModel.getRollNumber())
                    .password(studentModel.getPassword())
                    .authorities("ROLE_STUDENT")
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}

