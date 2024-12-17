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

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consultant")
public class ConsultantMaster {
	@Id
	private String id;

	@NotBlank(message = "Consultant Unique ID cannot be blank")
	private String consultantUniqueId;

	@NotBlank(message = "First Name cannot be blank")
	private String firstName;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email cannot be blank")
	private String email;

	@Size(max = 10, min = 10, message = "Contact number must be 10 digits")
	@NotBlank(message = "Contact number cannot be blank")
	private String contactNo;

	@Size(max = 50, message = "Address cannot exceed 50 characters")
	@NotBlank(message = "Address cannot be blank")
	private String address;

	private String lastName;
	private String password;
	private String keycloackId;

	// @Size(max = 10, min = 10, message = "Emergency contact number must be 10
	// digits")
	// @NotBlank(message = "Emergency contact number cannot be blank")
	private String emergencyContactNo;

	@Email(message = "Invalid emergency email format")
	@NotBlank(message = "Emergency email cannot be blank")
	private String emergencyEmail;

	@Email(message = "Invalid personal email format")
	@NotBlank(message = "Personal email cannot be blank")
	private String personalEmail;

	@NotBlank(message = "Date of joining cannot be blank")
	private String dateOfJoining;

	@NotNull(message = "Active status cannot be null")
	private Boolean isActive;

	@NotBlank(message = "Consultant type cannot be blank")
	private String consultantType;

	@Field(name = "photo")
	@NotBlank(message = "Photo URL cannot be blank")
	private String photoUrl;

	@Size(max = 12, min = 12, message = "Aadhaar number must be 12 digits")
	@NotBlank(message = "Aadhaar number cannot be blank")
	private String aadhaarNo;

	@NotBlank(message = "Aadhaar photo URL cannot be blank")
	private String aadhaarPhotoUrl;

	@Size(max = 10, min = 10, message = "PAN number must be 10 characters")
	@NotBlank(message = "PAN number cannot be blank")
	private String panNo;

	@NotBlank(message = "PAN photo URL cannot be blank")
	private String panPhotoUrl;

	@NotBlank(message = "Bank account number cannot be blank")
	private String bankAccountNo;

	@NotBlank(message = "IFSC code cannot be blank")
	private String ifscCode;

	@NotBlank(message = "Bank name cannot be blank")
	private String bankName;

	@NotBlank(message = "Passbook photo URL cannot be blank")
	private String passbookPhotoUrl;

	private List<String> consultantProjectDetailsId;

	private Set<String> projectId;
}
