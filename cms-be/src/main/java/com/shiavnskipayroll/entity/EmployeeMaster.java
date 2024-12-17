package com.shiavnskipayroll.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee_master")
public class EmployeeMaster {

    @Id
    private String id;

    @NotBlank(message = "Employee unique ID cannot be blank")
    private String employeeUniqueId;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String password;
    private String keycloackId;

    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    @NotBlank(message = "Contact number cannot be blank")
    private String contactNo;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    //@Size(min = 10, max = 10, message = "Emergency contact number must be 10 digits")
    //@NotBlank(message = "Emergency contact number cannot be blank")
    private String emergencyContactNo;

    //@Email(message = "Invalid email format")
    //@NotBlank(message = "Emergency email cannot be blank")
    private String emergencyEmail;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Personal email cannot be blank")
    private String personalEmail1;

    //@Email(message = "Invalid email format")
    private String personalEmail2;

    @NotBlank(message = "Position cannot be blank")
    private String position;

    @NotNull(message = "Date of joining cannot be null")
    private String dateOfJoining;  

    @NotNull(message = "Active status cannot be null")
    private Boolean isActive;

    @Field(name = "photo")
    private String photoUrl;

    @Size(min = 12, max = 12, message = "Aadhaar number must be 12 digits")
    @NotBlank(message = "Aadhaar number cannot be blank")
    private String aadhaarNo;

    private String aadhaarPhotoUrl;

    @Size(min = 10, max = 10, message = "PAN number must be 10 characters")
    @NotBlank(message = "PAN number cannot be blank")
    private String panNo;

    private String panPhotoUrl;

    @Size(min = 8, max = 8, message = "Passport number must be 8 characters")
    private String passportNo;

    private String drivingLicenseNo;
    
    private String voterId;

    @NotBlank(message = "Bank account number cannot be blank")
    private String bankAccountNo;

    @NotBlank(message = "IFSC code cannot be blank")
    private String ifscCode;

    @NotBlank(message = "Bank name cannot be blank")
    private String bankName;

    private String passbookPhotoUrl;

    //@NotNull(message = "CTC cannot be null")
    private Double ctc;

    //@NotNull(message = "PLI amount cannot be null")
    private Double pliAmount;

    //@NotNull(message = "Basic salary cannot be null")
    private String basic;

    //@NotNull(message = "PF amount cannot be null")
    private Double pf;

    //@NotNull(message = "Bonus amount cannot be null")
    private Double bonusAmount;

    private Set<String> projectId;
}
