package com.shiavnskipayroll.dto.response;

import lombok.Data;

@Data
public class AdminResponseDTO {
	
	private String id;

	private  String email;
	private String password;
	private  String firstName;
	private String lastName;
	private  String username;
	private  String role;

}
