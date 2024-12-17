package com.shiavnskipayroll.exceptions.global;

import com.mongodb.MongoSocketException;
import com.mongodb.MongoWriteException;
import com.shiavnskipayroll.exceptions.*;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;


@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// This exception is Thrown by Spring Boot When Validation Fails ,handle here
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		StringBuilder errorMessage = new StringBuilder("Validation errors: ");
		ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.append(error.getField()).append(": ")
				.append(error.getDefaultMessage()).append("; "));
		logger.error("Validation errors: {}", errorMessage.toString().trim());
		return new ResponseEntity<>(errorMessage.toString().trim(), HttpStatus.BAD_REQUEST);
	}
	 @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Invalid argument: " + ex.getMessage());
	    }
	    @ExceptionHandler(MongoSocketException.class)
	    public ResponseEntity<String> handleMongoSocketException(MongoSocketException ex) {
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	                .body("Unable to connect to the MongoDB server (Network issue): " + ex.getMessage());
	    }
	    @ExceptionHandler(DataIntegrityViolationException.class)
	    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("Data integrity violation: " + ex.getMessage());
	    }
        //This handler deals with OptimisticLockingFailureException, which occurs when multiple requests attempt to modify the same entity at the same time.
	    @ExceptionHandler(OptimisticLockingFailureException.class)
	    public ResponseEntity<String> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("Optimistic locking failure: " + ex.getMessage());
	    }

	    @ExceptionHandler(MongoWriteException.class)
	    public ResponseEntity<String> handleMongoWriteException(MongoWriteException ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("MongoDB write error: " + ex.getMessage());
	    }
	
	@ExceptionHandler(IOException.class)
	 public ResponseEntity<String> handleIOException(IOException ex) {
		logger.error("IOException occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing your request: " + ex.getMessage());
    }
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<String> handleMessagingException(MessagingException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Email sending failed: " + ex.getMessage());
	}
	@ExceptionHandler(FileProcessingException.class)
	public ResponseEntity<String> handleFileProcessingException(FileProcessingException ex) {
		// You can customize the response body as needed
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("File processing failed: " + ex.getMessage());
	}


	@ExceptionHandler(TimesheetEmployeeNotFoundException.class)
	public ResponseEntity<String> handleTimesheetEmployeeNotFound(TimesheetEmployeeNotFoundException ex) {
		logger.error("Timesheet Employee not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Timesheet Employee not found: "+ex.getMessage());
	}
	@ExceptionHandler(TimesheetProjectNotFoundException.class)
	public ResponseEntity<String> handleTimesheetProjectNotFound(TimesheetProjectNotFoundException ex) {
		logger.error("Timesheet Project not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Timesheet Project not found:"+ex.getMessage());
	} 

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
		logger.error("User not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found:"+ex.getMessage());
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
		logger.error("User already exists: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists: "+ex.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
		logger.error("Invalid credentials: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials: "+ex.getMessage());
	}

	@ExceptionHandler(TokenRefreshException.class)
	public ResponseEntity<String> handleTokenRefreshError(TokenRefreshException ex) {
		logger.error("Token refresh error: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<String> handleTokenExpired(TokenExpiredException ex) {
		logger.error("Token expired: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		logger.error("Resource not found: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConsultantNotFoundException.class)
	public ResponseEntity<String> handleConsultantNotFoundException(ConsultantNotFoundException ex) {
		logger.error("Consultant not found: {}", ex.getMessage());
		return new ResponseEntity<>("Consultant not found: "+ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException ex) {
		logger.error("Employee not found: {}", ex.getMessage());
		return new ResponseEntity<>("Employee not found: "+ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InternNotFoundException.class)
	public ResponseEntity<String> handleInternNotFound(InternNotFoundException ex) {
		logger.error("Intern not found: {}", ex.getMessage());
		return new ResponseEntity<>("Intern not found: "+ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<String> handleProjectNotFound(ProjectNotFoundException ex) {
		logger.error("Project not found: {}", ex.getMessage());
		return new ResponseEntity<>("Project not found: "+ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProjectRemovalFromEmployeeException.class)
	public ResponseEntity<String> handleProjectRemovalFromEmployee(ProjectRemovalFromEmployeeException ex) {
		logger.error("Project removal error from employee: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProjectRemovalFromInternException.class)
	public ResponseEntity<String> handleProjectRemovalFromIntern(ProjectRemovalFromInternException ex) {
		logger.error("Project removal error from intern: {}", ex.getMessage());
		return new ResponseEntity<>("Project removal error from intern: "+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProjectRemovalFromConsultantException.class)
	public ResponseEntity<String> handleProjectRemovalFromConsultant(ProjectRemovalFromConsultantException ex) {
		logger.error("Project removal error from consultant: {}", ex.getMessage());
		return new ResponseEntity<>("Project removal error from consultant: "+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TimesheetNotFoundException.class)
	public ResponseEntity<String> handleTimesheetNotFound(TimesheetNotFoundException ex) {
		logger.error("Timesheet not found: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProjectAssignmentFromEmployeeException.class)
	public ResponseEntity<String> handleProjectAssignmentFromEmployeeError(ProjectAssignmentFromEmployeeException ex) {
		logger.error("Project assignment error from employee: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProjectAssignmentFromInternException.class)
	public ResponseEntity<String> handleProjectAssignmentFromInternError(ProjectAssignmentFromInternException ex) {
		logger.error("Project assignment error from intern: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProjectAssignmentFromConsultantException.class)
	public ResponseEntity<String> handleProjectAssignmentFromConsultantError(
			ProjectAssignmentFromConsultantException ex) {
		logger.error("Project assignment error from consultant: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MailSendingException.class)
	public ResponseEntity<String> handleMailSendingError(MailSendingException ex) {
		logger.error("Mail sending error: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ExcelFileCreationException.class)
	public ResponseEntity<String> handleExcelFileCreationError(ExcelFileCreationException ex) {
		logger.error("Excel file creation error: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex) {
		logger.error("An unexpected error occurred: {}", ex.getMessage());
		return new ResponseEntity<>("An Unexpected error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RequestDtoNull.class)
	public ResponseEntity<String> handleGeneralException(RequestDtoNull ex) {
		logger.error("An unexpected error occurred: {}", ex.getMessage());
		return new ResponseEntity<>("An Unexpected error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvoiceNotFound.class)
	public ResponseEntity<String> handleGeneralException(InvoiceNotFound ex) {
		logger.error("An unexpected error occurred: {}", ex.getMessage());
		return new ResponseEntity<>("An Unexpected error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
