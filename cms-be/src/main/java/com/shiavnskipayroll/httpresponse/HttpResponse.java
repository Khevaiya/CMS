package com.shiavnskipayroll.httpresponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class HttpResponse <T> {
	    private int status;               // HTTP status code
	    private String message;           // Response message
	    private T data;                   // The data returned in the response
	    private LocalDateTime timestamp;  // Time of response
	    private List<String> errors;      // List of error messages (if any)

	    // Constructor for successful responses
	    public HttpResponse(int status, String message, T data) {
	        this.status = status;
	        this.message = message;
	        this.data = data;
	        this.timestamp = LocalDateTime.now(); // Set timestamp on creation
	        this.errors = null;                   // No errors for successful responses
	    }

	    // Constructor for error responses
	    public HttpResponse(int status, String message, List<String> errors) {
	        this.status = status;
	        this.message = message;
	        this.errors = errors;
	        this.timestamp = LocalDateTime.now(); // Set timestamp on creation
	        this.data = null;                      // No data for error responses
	    }
	    
}
