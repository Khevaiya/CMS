package com.shiavnskipayroll.service;

import com.shiavnskipayroll.dto.request.TimeSheetEmployeeRequestDto;
import com.shiavnskipayroll.dto.response.TimeSheetEmployeeResponseDto;

import java.util.List;

public interface TimeSheetEmployeeService {
	
	String createTimeSheet(TimeSheetEmployeeRequestDto requestDto);
	
	List<TimeSheetEmployeeResponseDto> getAllTimeSheets();
	
	List<TimeSheetEmployeeResponseDto> getAllById(String id);

	void deleteTimesheetById(String id);
	TimeSheetEmployeeResponseDto updateTimesheet(TimeSheetEmployeeRequestDto timeSheetEmployeeRequestDto,String id);

}
