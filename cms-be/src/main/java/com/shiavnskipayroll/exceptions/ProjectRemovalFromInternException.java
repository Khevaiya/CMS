package com.shiavnskipayroll.exceptions;

public class ProjectRemovalFromInternException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProjectRemovalFromInternException(String message) {
        super(message);
    }
}