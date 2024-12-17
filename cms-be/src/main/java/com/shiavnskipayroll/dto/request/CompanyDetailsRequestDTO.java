package com.shiavnskipayroll.dto.request;

import lombok.Data;

@Data
public class CompanyDetailsRequestDTO {

	private String companyName;
	private String companyAddress;
	private String city;
	private String state;
	private String country;
	private String pinCode;
	private String contactNumber1;
	private String contactNumber2;
	private String companyEmail1;
	private String companyEmail2;
	
	private String websiteUrl;
	private String registrationNumber;
	private String gstNumber;
	private String panNumber;
	
	private String description;
	private String companyLastWorkingDay;
	private String companyMonthlyExpenditure;

}
