package com.filestorage.s3_filestorage_app.exceptions.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.filestorage.s3_filestorage_app.exceptions.AccessDeniedToS3Exception;
import com.filestorage.s3_filestorage_app.exceptions.FileNotFoundInS3Exception;
import com.filestorage.s3_filestorage_app.exceptions.S3BucketNotFoundException;
import com.filestorage.s3_filestorage_app.exceptions.S3ServiceException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class S3ExceptionHandler {
	
	@ExceptionHandler(FileNotFoundInS3Exception.class)
    public ResponseEntity<Map<String, Object>> handleFileNotFoundInS3Exception(
    		FileNotFoundInS3Exception ex) {
		
        log.error("File not found in S3: ", ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(S3BucketNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleS3BucketNotFoundException(
    		S3BucketNotFoundException ex) {
    	
        log.error("S3 bucket not found: ", ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedToS3Exception.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedToS3Exception(
    		AccessDeniedToS3Exception ex) {
    	
        log.error("Access denied to S3: ", ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(S3ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleGenericS3Exception(
    		S3ServiceException ex) {
    	
        log.error("S3 service error: ", ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }
    
}
