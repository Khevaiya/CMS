package com.shiavnskipayroll.dto.request;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;
@Data
public class ConsultantMasterRequestDTO {

    private String consultantUniqueId;
    private String firstName;
    private String email;
    private String contactNo;
    private String address;
    private String emergencyContactNo;
    private String emergencyEmail;
    private String personalEmail;
    private String dateofjoining;
    private Boolean isActive;
    private String consultantType;
    @Field(name = "photo")
    private String photoUrl;

    private String lastName;
    private String password;

    private String aadhaarNo;
    private String aadhaarPhotoUrl;
    private String panNo;
    private String panPhotoUrl;
    private String bankAccountNo;
    private String ifscCode;
    private String bankName;
    private String passbookPhotoUrl;

    private List<String> consultantProjectDetailsId;
    private Set<String> projectId;

}
