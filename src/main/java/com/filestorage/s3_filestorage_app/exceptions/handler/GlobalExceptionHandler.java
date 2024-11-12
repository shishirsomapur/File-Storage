package com.filestorage.s3_filestorage_app.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {

		return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleBadRequest(HttpMessageNotReadableException ex) {

		String message = "Request body is missing or unreadable. Please provide a valid JSON body.";
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {

	    String errorMessage = "Validation error: " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

	}
	
}
