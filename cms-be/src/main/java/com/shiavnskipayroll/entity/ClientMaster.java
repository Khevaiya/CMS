package com.shiavnskipayroll.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "clients")
public class ClientMaster {
	@Id
	private String id;

	@NotBlank(message = "Client name cannot be blank")
	private String clientName;

	@Email(message = "Invalid email format for Contact Email 1")
	@NotBlank(message = "Contact Email 1 cannot be blank")
	private String clientContactEmailId1;

	private String clientContactEmailId2;

	@Size(min = 10, max = 10, message = "Contact Phone 1 must be 10 digits")
	@NotBlank(message = "Contact Phone 1 cannot be blank")
	private String clientContactPhone1;
private String clientContactPhone2;

	@NotBlank(message = "Client address cannot be blank")
	private String clientAddress;

	@Email(message = "Invalid email format for POC 1")
	@NotBlank(message = "POC 1 Email cannot be blank")
	private String poc1Email;

	@Size(min = 10, max = 10, message = "POC 1 Contact Number must be 10 digits")
	@NotBlank(message = "POC 1 Contact Number cannot be blank")
	private String poc1ContactNo;

	@Email(message = "Invalid email format for POC 2")
	@NotBlank(message = "POC 2 Email cannot be blank")
	private String poc2Email;

	@Size(min = 10, max = 10, message = "POC 2 Contact Number must be 10 digits")
	@NotBlank(message = "POC 2 Contact Number cannot be blank")
	private String poc2ContactNo;

	@NotBlank(message = "GST Number cannot be blank")
	private String gstNumber;

	@NotBlank(message = "PAN Number cannot be blank")
	private String panNumber;

	@NotBlank(message = "Company Website cannot be blank")
	private String companyWebsite;

	@NotNull(message = "Active status cannot be null")
	private Boolean isActive;

	@NotBlank(message="City can not be blank..")
	private String city;
	
	@NotBlank(message="State can not be blank..")
	private String state;

    private String clientSowSignedUrl; // URL or path to the file

//    @NotNull(message = "TDS Percentage cannot be null")
	private Double tdsPercentage;

//    @NotNull(message = "GST Percentage cannot be null")
	private Double gstPercentage;

	@NotBlank(message = "Created By cannot be blank")
	private String createdBy;

	@NotBlank(message = "Created Date cannot be blank")
	private String createdDate; // Consider changing to LocalDateTime

	@NotBlank(message = "Last Updated By cannot be blank")
	private String lastUpdatedBy;

	@NotBlank(message = "Last Updated Date cannot be blank")
	private String lastUpdatedDate; // Consider changing to LocalDateTime

	private List<String> listOfProjectIds;
}
