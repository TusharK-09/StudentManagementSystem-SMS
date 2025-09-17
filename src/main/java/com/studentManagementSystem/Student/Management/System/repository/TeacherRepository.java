package com.studentManagementSystem.Student.Management.System.repository;

import com.studentManagementSystem.Student.Management.System.model.TeacherModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends MongoRepository<TeacherModel , String> {
    Optional<TeacherModel> findByUsername(String username);
}

