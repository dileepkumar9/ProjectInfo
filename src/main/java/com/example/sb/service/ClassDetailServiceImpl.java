package com.example.sb.service;

import com.example.sb.model.ClassData;
import com.example.sb.model.ClassDetails;
import com.example.sb.repository.ClassDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassDetailServiceImpl implements ClassDetailService {

    @Autowired
    private ClassDetailsRepository classDetailsRepository;

    @Override
    public List<ClassDetails> getAllClassDetails() {
        return classDetailsRepository.findAll();
    }

    @Override
    public void saveClassDetail(ClassDetails classDetails) {

        classDetailsRepository.save(classDetails);
    }

    @Override
    public void deleteClassDetail(String className) {
        classDetailsRepository.deleteById(className);
    }
}
