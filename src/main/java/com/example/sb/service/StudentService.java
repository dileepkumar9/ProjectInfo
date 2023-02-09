package com.example.sb.service;

import java.util.List;
import com.example.sb.model.StudentDetails;

public interface StudentService {
	
	List<StudentDetails> getAllStudentDetails();

	StudentDetails getProfile(Long id);

	void saveProfile(StudentDetails studentDetails);

	void deleteEmployeeById(long id);


}
