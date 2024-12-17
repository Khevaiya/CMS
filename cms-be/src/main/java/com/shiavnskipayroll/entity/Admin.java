package com.shiavnskipayroll.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "admin")
public class Admin {
	@Id
	private String id;

	private  String email;
	private String password;
	private  String firstName;
	private String lastName;
	private  String username;
	private  String role;
	private  String keycloakId;
	
}
