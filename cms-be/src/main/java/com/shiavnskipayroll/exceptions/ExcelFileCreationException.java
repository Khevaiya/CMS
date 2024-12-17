package com.shiavnskipayroll.exceptions;

public class ExcelFileCreationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public ExcelFileCreationException(String message) {
        super(message);
    }

    public ExcelFileCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}