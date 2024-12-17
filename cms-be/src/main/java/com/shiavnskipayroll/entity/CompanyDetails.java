package com.shiavnskipayroll.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection ="CompanyDetails")
public class CompanyDetails {
	@Id
	private String id;
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
