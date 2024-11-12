package com.filestorage.s3_filestorage_app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;
import com.filestorage.s3_filestorage_app.service.s3.S3FileDownloadService;
import com.filestorage.s3_filestorage_app.service.s3.S3FileSearchService;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private S3FileSearchService fileSearchService;

    @Mock
    private S3FileDownloadService fileDownloadService;

    @InjectMocks
    private FileServiceImpl fileService;

    private FileRequestDTO fileRequestDTO;
    private FileResponseDTO fileResponseDTO;

    @BeforeEach
    public void setUp() {
    	
        fileRequestDTO = new FileRequestDTO();
        fileRequestDTO.setUserName("testUser");
        fileRequestDTO.setFileName("testFile.txt");

        fileResponseDTO = new FileResponseDTO();
    }

    @Test
    public void testSearchFile_FileFound() {
    	
        fileResponseDTO.setStatus("success");
        fileResponseDTO.setMessage("Files found successfully");

        when(fileSearchService.searchFileInS3(any(FileRequestDTO.class))).thenReturn(fileResponseDTO);

        FileResponseDTO result = fileService.searchFile(fileRequestDTO);

        assertEquals("success", result.getStatus());
        assertEquals("Files found successfully", result.getMessage());

        verify(fileSearchService).searchFileInS3(fileRequestDTO);
    }

    @Test
    public void testSearchFile_FileNotFound() {
    	
        fileResponseDTO.setStatus("not_found");
        fileResponseDTO.setMessage("No matching files found");

        when(fileSearchService.searchFileInS3(any(FileRequestDTO.class))).thenReturn(fileResponseDTO);

        FileResponseDTO result = fileService.searchFile(fileRequestDTO);

        assertEquals("not_found", result.getStatus());
        assertEquals("No matching files found", result.getMessage());

        verify(fileSearchService).searchFileInS3(fileRequestDTO);
    }

}
