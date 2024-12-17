package com.shiavnskipayroll.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PNLReportResponseDTO {

	private String projectId;
	private String projectName;
	private Double baseAmount;
	private Double totalCosts;
	private Double netEarnings;
	private Boolean isProfitable=false;
	
	
}
