package com.filestorage.s3_filestorage_app.exceptions;

public class S3BucketNotFoundException extends S3ServiceException {
	
    public S3BucketNotFoundException(String message) {
        super(message);
    }
}
