package com.shiavnskipayroll.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TimesheetEmployee")
public class TimesheetEmployee {

	@Id
	private String id;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotBlank(message = "Date can not be blank")
	private String date;



	@Size(max = 500)
	@NotBlank(message = "Description can not be blank")
	private String taskCompleted;

	@Size(min = 2, max = 8, message = "Hours Worked must be 2-8 digits")
	@NotBlank(message = "Hours Worked can not be blank")
	private Double hoursWorked;

	@NotBlank(message = "EmployeeId can not be blank")
	private String employeeId;
}
