package com.shiavnskipayroll.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shiavnskipayroll.dto.response.EmployeeMasterResponseDTO;
import  com.shiavnskipayroll.entity.EmployeeMaster;


@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeMaster, String> {
	boolean existsByEmail(String email);
	 @Query("{ 'projectId': { $ne: null, $not: { $size: 0 } } }")
	    List<EmployeeMasterResponseDTO> findByProjectIdIsNotNullAndProjectIdIsNotEmpty();
EmployeeMaster findByEmail(String email);//call in timesheet projects
}
