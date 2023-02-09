package com.example.sb.repository;

import com.example.sb.model.StudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentDetails, Long>{

}
