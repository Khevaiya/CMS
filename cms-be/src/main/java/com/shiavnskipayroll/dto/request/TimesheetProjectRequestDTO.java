package com.shiavnskipayroll.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimesheetProjectRequestDTO {
	
	    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
	    private String date;
	    private double hoursWorked;
	    private String taskCompleted;
	    private String  employeeId;
	    private String  projectId;

}
