package com.slashcode.supportticket.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.slashcode.supportticket.response.ApiResponse;
import com.slashcode.supportticket.util.ResponseUtils;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException exception){
		
		Map<String, String> errors = new HashMap<>();
		
		exception.getBindingResult()
			.getFieldErrors()
			.forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage())
					);
		
		return ResponseUtils.getResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
	}
	
	@ExceptionHandler({UserAlreadyExistException.class, CategoryAlreadyExistsException.class})
	public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistException(RuntimeException exception){
		return ResponseUtils.getResponse(HttpStatus.CONFLICT, exception.getMessage(), null);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(ResourceNotFoundException exception){		
		return ResponseUtils.getResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
	}
	
	@ExceptionHandler({InvalidTicketStatusException.class, InvalidTicketAssignmentException.class, InvalidCommentVisibilityException.class, IllegalArgumentException.class})
	public ResponseEntity<ApiResponse<Void>> handleBadRequestExceptions(RuntimeException exception){		
		return ResponseUtils.getResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex){
		return ResponseUtils.getResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password", null);
	}
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException ex){
		return ResponseUtils.getResponse(HttpStatus.FORBIDDEN, "You do not have permission to access, please contact admin", null);
	}
	
	// generic exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception exception){
		logger.error("An unexpected error occurred ", exception);
		return ResponseUtils.getResponse(HttpStatus.INTERNAL_SERVER_ERROR, "There is some issue at server side, please contact admin.", null);
	}
	
}
