package com.shiavnskipayroll.repository;

import com.shiavnskipayroll.dto.response.TimeSheetEmployeeResponseDto;
import com.shiavnskipayroll.entity.TimesheetEmployee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSheetEmployeeRepository extends MongoRepository<TimesheetEmployee, String> {
	List<TimesheetEmployee> findAllByEmployeeId(String employeeId);
	List<TimeSheetEmployeeResponseDto> findAllByDateStartingWith(String yearMonth);
	List<TimeSheetEmployeeResponseDto> findAllByEmployeeIdAndDateStartingWith(String employeeId, String yearMonth);
}
