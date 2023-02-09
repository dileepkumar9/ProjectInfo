package com.example.sb.service;

import com.example.sb.model.Discipline;
import java.util.List;

public interface DisciplineService {

    List<Discipline> getAllDiscipline();

    Discipline getDiscipline(int id);
}
