package com.shiavnskipayroll.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class EmployeeMasterResponseDTO {
    private String id;
    private String employeeUniqueId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private String address;
    private String state;
    private String city;
    private String country;
    private String emergencyContactNo;
    private String emergencyEmail;
    private String personalEmail1;
    private String personalEmail2;
   
    private String position;
    private String dateofjoining;
    private Boolean isActive;
    private String photoUrl;  // Assuming photo is stored separately and you provide a URL

    private String aadhaarNo;
    private String aadhaarPhotoUrl;
    private String panNo;
    private String panPhotoUrl;
    private String passportNo;
    private String drivingLicenseNo;
    private String voterId;
    private String bankAccountNo;
    private String ifscCode;
    private String bankName;
    private String passbookPhotoUrl;

    private Double ctc;
    private Double pliAmount;
    private String basic;
    private Double pf;
    private Double bonusAmount;

    private Set<String> projectId;

}
