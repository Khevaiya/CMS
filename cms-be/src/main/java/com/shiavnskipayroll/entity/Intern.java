package com.shiavnskipayroll.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "intern")
public class Intern {


    private String lastName;
    private String password;
    private String keycloackId;

    @Id
    private String id;

    @NotBlank(message = "Intern unique ID is mandatory")
    private String internUniqueId;

    @NotBlank(message = "Intern name is mandatory")
    @Size(max = 100, message = "Intern name must be less than 100 characters")
    private String internName;

    @NotBlank(message = "Personal email is mandatory")
    @Email(message = "Personal email should be valid")
    private String personalEmail;

    @NotBlank(message = "Contact number is mandatory")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
    private String contactNo;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Intern type is mandatory")
    private String internType;

    @NotBlank(message = "Date of joining is mandatory")
    private String dateOfJoining;

    @NotNull(message = "Active status is mandatory")
    private Boolean isActive;

    @NotBlank(message = "Aadhaar number is mandatory")
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar number must be 12 digits")
    private String aadhaarNo;

    @NotBlank(message = "PAN number is mandatory")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "PAN number format is invalid")
    private String panNo;

   

    @NotBlank(message = "Bank account number is mandatory")
    private String bankAccountNo;

    @NotBlank(message = "IFSC code is mandatory")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC code format is invalid")
    private String ifscCode;

    @NotBlank(message = "Bank name is mandatory")
    private String bankName;

    @NotNull(message = "Stipend is mandatory")
    @DecimalMin(value = "0.0", message = "Stipend must be greater than or equal to 0")
    private Double stipend;

    @NotNull(message = "Basic is mandatory")
    @DecimalMin(value = "0.0", message = "Basic must be greater than or equal to 0")
    private Double basic;

    @NotNull(message = "PF is mandatory")
    @DecimalMin(value = "0.0", message = "PF must be greater than or equal to 0")
    private Double pf;

    @NotNull(message = "Bonus amount is mandatory")
    @DecimalMin(value = "0.0", message = "Bonus amount must be greater than or equal to 0")
    private Double bonusAmount;

    private String photoUrl;
    private String aadhaarPhotoUrl;
    private String recentMarksheetOrDegreePhotoUrl;
    private String panPhotoUrl;
    private String passbookPhotoUrl;

    private Set<String> projectId = new HashSet<>();
}
