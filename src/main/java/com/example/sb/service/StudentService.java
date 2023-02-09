package com.example.sb.service;

import java.util.List;
import com.example.sb.model.StudentDetails;

public interface StudentService {
	
	List<StudentDetails> getAllStudentDetails();

	StudentDetails getStudent(Long id);

	void saveStudent(StudentDetails studentDetails);

	void deleteStudentById(long id);

}
