package com.shiavnskipayroll.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


@Data
public class TimesheetProjectResponseDTO {
 
    private String id;
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private String date;
    private double hoursWorked;
    private String taskCompleted;
    private String  employeeId;
    private String  projectId; 
}
