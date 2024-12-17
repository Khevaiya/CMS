package com.shiavnskipayroll.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consultantProjectDetails")

public class ConsultantProjectDetails {
	@Id
	private String id;

	@NotBlank(message = "Position can not be blank")
	private String position;
	
	@NotBlank(message = "Duration can not be blank ")
	private String duration;
	
	@NotBlank(message = "CompensationType can not be blank ")
	private String compensationType;
	
	@NotBlank(message = "Commission can not be blank ")
	private String commission;
	
	@NotBlank(message = "TDS can not be blank ")
	private String tds;
	
	@NotBlank(message = "DonusAmount can not be blank ")
	private String bonusAmount;
	
	@NotBlank(message = "Remark can not be blank ")
	private String remark;
	
	@NotBlank(message = "ConsultantId can not be blank ")
	private String consultantId;
	
	// @NotBlank(message = "ProjectId can not be blank ")
	private String projectId;// change

}
