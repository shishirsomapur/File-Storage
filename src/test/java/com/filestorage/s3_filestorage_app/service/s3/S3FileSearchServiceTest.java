package com.filestorage.s3_filestorage_app.service.s3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.filestorage.s3_filestorage_app.constant.FileServiceConstants;
import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;
import com.filestorage.s3_filestorage_app.exceptions.S3ServiceException;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

@ExtendWith(MockitoExtension.class)
public class S3FileSearchServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3FileSearchService s3FileSearchService;

    private FileRequestDTO fileRequestDTO;

    @BeforeEach
    public void setUp() {
        fileRequestDTO = new FileRequestDTO();
        fileRequestDTO.setUserName("testUser");
        fileRequestDTO.setFileName("testFile.txt");
    }

    @Test
    public void testSearchFileInS3_FileFound() {
        
        S3Object s3Object = S3Object.builder()
                .key("testUser/testFile.txt")
                .build();
        ListObjectsV2Response listObjectsV2Response = ListObjectsV2Response.builder()
                .contents(Arrays.asList(s3Object))
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsV2Response);

        FileResponseDTO result = s3FileSearchService.searchFileInS3(fileRequestDTO);

        assertEquals(FileServiceConstants.STATUS_SUCCESS, result.getStatus());
        assertEquals(1, result.getFiles().size());
        assertEquals("testFile.txt", result.getFiles().get(0));
        verify(s3Client).listObjectsV2(any(ListObjectsV2Request.class));
    }

    @Test
    public void testSearchFileInS3_FileNotFound() {
        
    	ListObjectsV2Response listObjectsV2Response = ListObjectsV2Response.builder()
                .contents(Arrays.asList())
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsV2Response);

        FileResponseDTO result = s3FileSearchService.searchFileInS3(fileRequestDTO);

        assertEquals(FileServiceConstants.STATUS_NOT_FOUND, result.getStatus());
        assertEquals("No matching files found for the filename testFile.txt", result.getMessage());
        verify(s3Client).listObjectsV2(any(ListObjectsV2Request.class));
    }

    @Test
    public void testSearchFileInS3_S3Exception() {
    	
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenThrow(RuntimeException.class);

        S3ServiceException exception = org.junit.jupiter.api.Assertions.assertThrows(S3ServiceException.class, () -> {
            s3FileSearchService.searchFileInS3(fileRequestDTO);
        });

        assertEquals("An error occurred while accessing S3. Please try again later.", exception.getMessage());
        verify(s3Client).listObjectsV2(any(ListObjectsV2Request.class));
    }
}
