package com.filestorage.s3_filestorage_app.exceptions;

public class FileNotFoundInS3Exception extends S3ServiceException {
	
    public FileNotFoundInS3Exception(String message) {
        super(message);
    }
    
}
