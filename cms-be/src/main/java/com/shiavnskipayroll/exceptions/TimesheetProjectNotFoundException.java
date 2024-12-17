package com.shiavnskipayroll.exceptions;

public class TimesheetProjectNotFoundException extends RuntimeException  {
	 private static final long serialVersionUID = 1L;

	    public TimesheetProjectNotFoundException(String message) {
	        super(message);
	    }
}
