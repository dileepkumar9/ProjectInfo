package com.example.sb.service;

import java.util.List;
import com.example.sb.model.ClassData;

public interface ClassDataService {

    List<ClassData> getAllClassData();

    ClassData getClassData(Long id);

    void saveClassData(ClassData classData);

    void deleteClassData(Long rollNo);
}
