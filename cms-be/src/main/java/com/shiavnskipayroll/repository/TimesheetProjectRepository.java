package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.dto.response.TimesheetProjectResponseDTO;
import com.shiavnskipayroll.entity.TimesheetProject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimesheetProjectRepository extends MongoRepository<TimesheetProject, String> {
	 List<TimesheetProject> findByEmployeeId(String employeeId);
	 List<TimesheetProject> findByProjectId(String projectId);
	List<TimesheetProjectResponseDTO> findAllByProjectIdAndDateStartingWith(String projectId, String yearMonth);


}