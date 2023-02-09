package com.example.sb.service;

import java.util.List;

import com.example.sb.model.StudentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sb.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository pr;

    @Override
    public List<StudentDetails> getAllStudentDetails() {
        return pr.findAll();
    }

    @Override
    public StudentDetails getStudent(Long id) {
        return pr.findById(id).orElse(null);
    }

    @Override
    public void saveStudent(StudentDetails studentDetails) {
        this.pr.save(studentDetails);
    }

    @Override
    public void deleteStudentById(long id) {
        this.pr.deleteById(id);
    }

}
