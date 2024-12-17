package com.shiavnskipayroll.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ProjectNotFoundException(String projectId) {
        super("Project with ID " + projectId + " not found.");
    }
}