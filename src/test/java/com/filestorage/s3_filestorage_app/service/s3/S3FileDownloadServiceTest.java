package com.filestorage.s3_filestorage_app.service.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.filestorage.s3_filestorage_app.constant.FileServiceConstants;
import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.exceptions.FileNotFoundInS3Exception;
import com.filestorage.s3_filestorage_app.exceptions.S3ServiceException;

import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ExtendWith(MockitoExtension.class)
public class S3FileDownloadServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3FileDownloadService s3FileDownloadService;
    
    @Test
    public void testDownloadFileFromS3_FileNotFoundException() {

    	FileRequestDTO fileRequestDTO = new FileRequestDTO("user1", "nonexistentfile.txt");

        AwsErrorDetails mockAwsErrorDetails = mock(AwsErrorDetails.class);
        when(mockAwsErrorDetails.errorCode()).thenReturn(FileServiceConstants.NO_SUCH_KEY);

        S3Exception mockS3Exception = mock(S3Exception.class);
        when(mockS3Exception.awsErrorDetails()).thenReturn(mockAwsErrorDetails);

        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(mockS3Exception);

        FileNotFoundInS3Exception exception = assertThrows(FileNotFoundInS3Exception.class, () -> {
            s3FileDownloadService.downloadFileFromS3(fileRequestDTO);
        });

        assertEquals("File not found in S3 for the specified filename.", exception.getMessage());
    }


    @Test
    public void testDownloadFileFromS3_AccessDeniedException() {
    	
        FileRequestDTO fileRequestDTO = new FileRequestDTO("user1", "file.txt");

        S3ServiceException exception = assertThrows(S3ServiceException.class, () -> {
            s3FileDownloadService.downloadFileFromS3(fileRequestDTO);
        });

        assertEquals("Network issue while accessing S3.", exception.getMessage());
    }
}
