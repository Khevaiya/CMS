package com.shiavnskipayroll.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document(collection = "TimesheetProject")
public class TimesheetProject {
	@Id
	private String id;

	@NotNull(message = "Basic salary cannot be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String date;

	@Max(12)
	@NotNull(message = "Hours Worked can not blank")
	private double hoursWorked;

	@NotBlank(message = "Task Completed can not be blank")
	private String taskCompleted;



	@NotBlank(message = "EmployeeId")
	private String employeeId;

	@NotBlank(message = "Project Id can not be blank")
	private String projectId;

}
