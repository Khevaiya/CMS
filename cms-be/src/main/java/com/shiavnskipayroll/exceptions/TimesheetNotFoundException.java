package com.shiavnskipayroll.exceptions;

public class TimesheetNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TimesheetNotFoundException(String message) {
        super(message);
    }
}