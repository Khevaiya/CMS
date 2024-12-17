package com.shiavnskipayroll.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetEmployeeResponseDto {

	private String id;

	private String date;

	private double hoursWorked;



	private String taskCompleted;

	private String employeeId;

}
