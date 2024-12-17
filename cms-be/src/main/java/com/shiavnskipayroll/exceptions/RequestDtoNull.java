package com.shiavnskipayroll.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestDtoNull extends RuntimeException{
	
	public final String message;
}
