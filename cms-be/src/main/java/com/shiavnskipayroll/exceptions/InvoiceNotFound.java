package com.shiavnskipayroll.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvoiceNotFound extends RuntimeException{
	private final String message;
}
