package com.studentManagementSystem.Student.Management.System.repository;

import com.studentManagementSystem.Student.Management.System.model.StudentModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<StudentModel , String> {
    Optional<StudentModel> findByRollNumber(String rollNumber);
}
