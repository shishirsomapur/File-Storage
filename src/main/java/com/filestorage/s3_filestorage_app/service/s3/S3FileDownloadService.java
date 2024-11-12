package com.filestorage.s3_filestorage_app.service.s3;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.filestorage.s3_filestorage_app.constant.FileServiceConstants;
import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.exceptions.AccessDeniedToS3Exception;
import com.filestorage.s3_filestorage_app.exceptions.FileNotFoundInS3Exception;
import com.filestorage.s3_filestorage_app.exceptions.S3BucketNotFoundException;
import com.filestorage.s3_filestorage_app.exceptions.S3ServiceException;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@Slf4j
public class S3FileDownloadService {
    
	private final S3Client s3Client;
    
    public S3FileDownloadService(S3Client s3Client) {
    	
        this.s3Client = s3Client;
    }

    public InputStreamResource downloadFileFromS3(FileRequestDTO fileRequestDTO) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(FileServiceConstants.S3_BUCKET_NAME)
                    .key(fileRequestDTO.getUserName() + "/" + fileRequestDTO.getFileName())
                    .build();
            ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(request);
            return new InputStreamResource(s3ObjectStream);
        } catch (S3Exception e) {
            handleS3Exception(e, fileRequestDTO);
        } catch (Exception e) {
            log.error("Network issue while accessing S3.", e);
            throw new S3ServiceException("Network issue while accessing S3.");
        }
        return null;
    }

    private void handleS3Exception(S3Exception e, 
    		FileRequestDTO fileRequestDTO) {
    	
        switch (e.awsErrorDetails().errorCode()) {
        
            case FileServiceConstants.NO_SUCH_KEY:
                log.error("File not found in S3: " + fileRequestDTO.getFileName(), e);
                throw new FileNotFoundInS3Exception("File not found in S3 for the specified filename.");
                
            case FileServiceConstants.NO_SUCH_BUCKET:
                log.error("Bucket not found: " + FileServiceConstants.S3_BUCKET_NAME, e);
                throw new S3BucketNotFoundException("The requested S3 bucket does not exist.");
                
            case FileServiceConstants.ACCESS_DENIED:
                log.error("Access denied to bucket or file: " + FileServiceConstants.S3_BUCKET_NAME, e);
                throw new AccessDeniedToS3Exception("Access denied to the requested file.");
                
            default:
                log.error("Error occurred while accessing the S3 service.", e);
                throw new S3ServiceException("An error occurred while accessing S3.");
                
        }
        
    }

}
