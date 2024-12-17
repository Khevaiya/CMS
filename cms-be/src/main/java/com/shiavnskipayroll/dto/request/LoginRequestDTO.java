package com.shiavnskipayroll.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String password;
    private  String username;
}
