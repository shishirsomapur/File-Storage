package com.filestorage.s3_filestorage_app.exceptions;

public class S3ServiceException extends RuntimeException {
	
    public S3ServiceException(String message) {
        super(message);
    }
    
}
