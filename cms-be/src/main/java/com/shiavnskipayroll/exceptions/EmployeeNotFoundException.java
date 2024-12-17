package com.shiavnskipayroll.exceptions;

public class EmployeeNotFoundException  extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public EmployeeNotFoundException(String employeeId) {
        super("Employee with ID " + employeeId + " not found.");
    }
}