package com.shiavnskipayroll.exceptions;

import java.io.Serial;

public class TimesheetEmployeeNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public TimesheetEmployeeNotFoundException(String message) {
        super(message);
    }
}
