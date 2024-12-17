package com.shiavnskipayroll.dto.response;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class InternResponseDTO {
	private String id;
	private String internUniqueId;
	private String internName;
	private String lastName;
	private String personalEmail;
	private String contactNo;
	private String address;
	private String internType;
	private String dateOfJoining;
	private Boolean isActive;
	private String aadhaarNo;
	private String panNo;
	
	private String bankAccountNo;
	private String ifscCode;
	private String bankName;
	private Double stipend;
	private Double basic;
	private Double pf;
	private Double bonusAmount;

	private String photoUrl;
	private String aadhaarPhotoUrl;
	private String recentMarksheetOrDegreePhotoUrl;
	private String panPhotoUrl;
	private String passbookPhotoUrl;

	private Set<String> projectId=new HashSet<>();
    

}