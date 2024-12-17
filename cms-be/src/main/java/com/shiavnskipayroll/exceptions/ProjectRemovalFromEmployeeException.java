package com.shiavnskipayroll.exceptions;

public class ProjectRemovalFromEmployeeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProjectRemovalFromEmployeeException(String message) {
        super(message);
    }
}