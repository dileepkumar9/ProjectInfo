package com.example.sb.service;

import com.example.sb.model.Discipline;
import com.example.sb.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineServiceImpl implements DisciplineService {

    @Autowired
    DisciplineRepository disciplineRepository;

    @Override
    public List<Discipline> getAllDiscipline() {
        return disciplineRepository.findAll();
    }

    @Override
    public Discipline getDiscipline(int id) {
        return disciplineRepository.findById(id).orElse(null);
    }
}
