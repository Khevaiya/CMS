package com.shiavnskipayroll.exceptions;

public class MailSendingException extends RuntimeException {
    private static final long serialVersionUID = 1L; // This is a best practice for Serializable classes

    public MailSendingException(String message) {
        super("Mail sending error: " + message); // Calls the constructor of RuntimeException
    }
    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}