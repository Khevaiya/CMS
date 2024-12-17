package com.shiavnskipayroll.dto.request;

import lombok.Data;

@Data
public class AdminRegisterRequestDTO {
    private  String email;
    private String password;
    private  String firstName;
    private String lastName;
    private  String username;
    private  String role;
}
