package com.example.sb.service;

import com.example.sb.model.ClassData;
import com.example.sb.repository.ClassDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassDataServiceImpl implements ClassDataService {

    @Autowired
    private ClassDataRepository classDataRepository;


    @Override
    public List<ClassData> getAllClassData() {
        return classDataRepository.findAll();
    }

    @Override
    public ClassData getClassData(Long id) {
        return classDataRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClassData(ClassData classData) {
        classDataRepository.save(classData);
    }

    @Override
    public void deleteClassData(Long rollNo) {
        classDataRepository.deleteById(rollNo);
    }


}
