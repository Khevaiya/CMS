package com.shiavnskipayroll.exceptions;

public class FileProcessingException extends  RuntimeException{
    private static final long serialVersionUID = 1L;
    public FileProcessingException(String message) {
        super(message);
    }
}
