package com.example.sb.service;

import com.example.sb.model.ClassDetails;

import java.util.List;

public interface ClassDetailService {

    List<ClassDetails> getAllClassDetails();

    void saveClassDetail(ClassDetails classDetails);

    void deleteClassDetail(String className);
}
