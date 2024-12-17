package com.shiavnskipayroll.exceptions;

public class ClientNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ClientNotFoundException(String clientId) {
        super("Client with ID " + clientId + " not found.");
    }
}