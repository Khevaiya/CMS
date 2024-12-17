package com.shiavnskipayroll.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class ProjectMasterRequestDTO {
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
	private Set<String> InternId;

}
