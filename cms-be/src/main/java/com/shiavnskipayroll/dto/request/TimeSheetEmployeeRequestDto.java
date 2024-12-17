package com.shiavnskipayroll.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheetEmployeeRequestDto {

	@NotBlank(message = "TimeSheet Date can not be null..")
	private String date;

	@Max(8)
	@NotBlank(message = "Hours Worked can not be blank..")
	private double hoursWorked;



	@Size(max = 500)
	@NotBlank(message = "taskCompleted can not be blank..")
	private String taskCompleted;

	@NotBlank(message = "EmployeeId can not be null..")
	private String employeeId;
}
