package com.shiavnskipayroll.exceptions;

public class ConsultantNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ConsultantNotFoundException(String consultantId) {
        super("Consultant with ID " + consultantId + " not found.");
    }
}