package com.shiavnskipayroll.exceptions;

public class ProjectRemovalFromConsultantException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProjectRemovalFromConsultantException(String message) {
        super(message);
    }
}