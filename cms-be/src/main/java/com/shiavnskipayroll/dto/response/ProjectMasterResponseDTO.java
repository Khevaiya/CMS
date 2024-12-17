package com.shiavnskipayroll.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class ProjectMasterResponseDTO {
    private String id;
    private String clientId;
    private String projectName;
    private String description;
    private String startDate;
    private String endDate;
    private String rateCard;
    private String resourceAllocation;
    private String reportingManager;

    private String candidateType;
    private String interviewdClearedCandidateId;
    private String lastWorkingDayOfMonth;

    private Set<String> employeeId;
    private Set<String> consultantId;
    private Set<String> internId;
}
