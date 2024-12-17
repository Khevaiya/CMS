package com.shiavnskipayroll.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomException extends RuntimeException {
	private final String message;
}
