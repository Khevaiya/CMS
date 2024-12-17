package com.shiavnskipayroll.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "projects")
public class ProjectMaster {

    @Id
    private String id;

    @NotBlank(message = "Client ID cannot be blank")
    private String clientId;

    @NotBlank(message = "Project name cannot be blank")
    private String projectName;

    @NotBlank(message = "Client ID cannot be blank")
    private String description;

    @NotBlank(message = "Start date cannot be null")
    private String startDate; 

    @NotBlank(message = "End Date date cannot be null")
    private String endDate; 

    @NotBlank(message = "Rate Card cannot be null")
    private String rateCard;  

    //@NotBlank(message = "Resource Allocation cannot be null")
    private String resourceAllocation;  

    @NotBlank(message = "Reporting Manager cannot be null")
    private String reportingManager;

    @NotBlank(message = "Candidate Type cannot be null")
    private String candidateType;

    @NotBlank(message = "Interviewd Cleared Candidate Id cannot be null")
    private String interviewdClearedCandidateId;

    @NotBlank(message = "Last Working Day Of Month cannot be null")
    private String lastWorkingDayOfMonth;  
    
    private String projectFileUrl1;
    private String projectFileUrl2;

    private Set<String> employeeId;  
    private Set<String> consultantId;  
    private Set<String> internId;  
}
