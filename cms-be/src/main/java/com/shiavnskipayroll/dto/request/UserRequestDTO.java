package com.shiavnskipayroll.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {
	private String email;

	private String password;

	private String firstName;

	private String lastname;

	private String username;

	private String role;

}
