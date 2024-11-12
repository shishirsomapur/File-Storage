package com.filestorage.s3_filestorage_app.exceptions;

public class AccessDeniedToS3Exception extends S3ServiceException {
	
    public AccessDeniedToS3Exception(String message) {
        super(message);
    }
    
}
