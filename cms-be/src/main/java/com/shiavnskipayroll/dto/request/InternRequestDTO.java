package com.shiavnskipayroll.dto.request;

import lombok.Data;

import java.util.Set;
@Data
public class InternRequestDTO {

    private String internUniqueId;
    private String internName;
    private String personalEmail;
    private String contactNo;
    private String address; 
    private String internType;
    private String dateOfJoining;
    private Boolean isActive;

    private String lastName;
    private String password;



    private String photoUrl;
   
    private String aadhaarNo;

    private String aadhaarPhotoUrl;
    private String panNo;
    private String panPhotoUrl;

   
    private  String marksheetPhotoUrl;
    
    private String bankAccountNo;
    private String ifscCode;
    private String bankName;
    private String passbookPhotoUrl;

    private Double stipend;
   
    private Double basic;
    private Double pf;
    private Double bonusAmount;

    private Set<String> projectId;
    

}
