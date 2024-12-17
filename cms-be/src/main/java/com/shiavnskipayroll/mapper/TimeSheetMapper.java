package com.shiavnskipayroll.mapper;

import com.shiavnskipayroll.dto.request.TimeSheetEmployeeRequestDto;
import com.shiavnskipayroll.dto.response.TimeSheetEmployeeResponseDto;
import com.shiavnskipayroll.entity.TimesheetEmployee;
import com.shiavnskipayroll.exceptions.RequestDtoNull;

public class TimeSheetMapper {

	private TimeSheetMapper() {
		throw new IllegalArgumentException("Utility Class");
	}

	public static TimesheetEmployee toEntity(TimeSheetEmployeeRequestDto requestDto) {
		if (requestDto == null) {
			throw new RequestDtoNull("Time Sheet Not Found In TimeSheetEmployeeRequestDto");
		}

		TimesheetEmployee timeSheet = new TimesheetEmployee();
		timeSheet.setDate(requestDto.getDate());
		timeSheet.setTaskCompleted(requestDto.getTaskCompleted());
		timeSheet.setEmployeeId(requestDto.getEmployeeId());
		timeSheet.setHoursWorked(requestDto.getHoursWorked());


		return timeSheet;
	}

	public static TimeSheetEmployeeResponseDto toRDto(TimesheetEmployee timeSheet) {
		if (timeSheet == null) {
			throw new RequestDtoNull("Time Sheet Not Found In TimesheetEmployee");
		}
		TimeSheetEmployeeResponseDto responseDto = new TimeSheetEmployeeResponseDto();
		responseDto.setDate(timeSheet.getDate());
		responseDto.setTaskCompleted(timeSheet.getTaskCompleted());
		responseDto.setHoursWorked(timeSheet.getHoursWorked());

		responseDto.setId(timeSheet.getId());
		responseDto.setEmployeeId(timeSheet.getEmployeeId());

		return responseDto;
	}
}
