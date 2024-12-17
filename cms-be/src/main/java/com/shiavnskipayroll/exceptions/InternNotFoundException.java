package com.shiavnskipayroll.exceptions;

public class InternNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InternNotFoundException(String internId) {
        super("Intern with ID " + internId + " not found.");
    }
}