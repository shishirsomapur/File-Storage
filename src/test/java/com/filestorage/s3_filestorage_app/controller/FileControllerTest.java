package com.filestorage.s3_filestorage_app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;
import com.filestorage.s3_filestorage_app.pojo.FileRequest;
import com.filestorage.s3_filestorage_app.pojo.FileResponse;
import com.filestorage.s3_filestorage_app.service.interfaces.FileService;
import com.filestorage.s3_filestorage_app.exceptions.FileNotFoundInS3Exception;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private FileService fileService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FileController fileController;

    private FileRequest fileRequest;
    private FileRequestDTO fileRequestDTO;
    private FileResponseDTO fileResponseDTO;
    private FileResponse fileResponse;
    private InputStreamResource resource;

    @BeforeEach
    public void setup() {
    	
        fileRequest = new FileRequest();
        fileRequest.setUserName("testUser");
        fileRequest.setFileName("testFile.txt");

        fileRequestDTO = new FileRequestDTO();
        fileRequestDTO.setUserName("testUser");
        fileRequestDTO.setFileName("testFile.txt");

        fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setStatus("success");
        fileResponseDTO.setMessage("Files found successfully");

        fileResponse = new FileResponse();
        fileResponse.setStatus("success");
        fileResponse.setMessage("Files found successfully");

        resource = new InputStreamResource(System.in);
    }

    @Test
    public void testSearchFile_Success() {

    	when(modelMapper.map(fileRequest, FileRequestDTO.class)).thenReturn(fileRequestDTO);
        when(fileService.searchFile(fileRequestDTO)).thenReturn(fileResponseDTO);
        when(modelMapper.map(fileResponseDTO, FileResponse.class)).thenReturn(fileResponse);

        FileResponse result = fileController.searchFile(fileRequest);

        assertEquals("success", result.getStatus());
        assertEquals("Files found successfully", result.getMessage());
    }

    @Test
    public void testSearchFile_FileNotFound() {
    	
        fileResponseDTO.setStatus("not_found");
        fileResponseDTO.setMessage("No matching files found");

        when(modelMapper.map(fileRequest, FileRequestDTO.class)).thenReturn(fileRequestDTO);
        when(fileService.searchFile(fileRequestDTO)).thenReturn(fileResponseDTO);
        when(modelMapper.map(fileResponseDTO, FileResponse.class)).thenReturn(fileResponse);
        
        fileResponse.setStatus("not_found");
        fileResponse.setMessage("No matching files found");

        FileResponse result = fileController.searchFile(fileRequest);

        assertEquals("not_found", result.getStatus());
        assertEquals("No matching files found", result.getMessage());
    }


    @Test
    public void testDownloadFile_Success() {

    	when(modelMapper.map(fileRequest, FileRequestDTO.class)).thenReturn(fileRequestDTO);
        when(fileService.downloadFile(fileRequestDTO)).thenReturn(resource);

        ResponseEntity<InputStreamResource> responseEntity = fileController.downloadFile(fileRequest);

        assertEquals(MediaType.APPLICATION_OCTET_STREAM, responseEntity.getHeaders().getContentType());
        assertEquals("attachment; filename=\"" + fileRequest.getFileName() + "\"",
                responseEntity.getHeaders().getFirst("Content-Disposition"));
        assertEquals(resource, responseEntity.getBody());
    }

    @Test
    public void testDownloadFile_FileNotFoundException() {

    	when(modelMapper.map(fileRequest, FileRequestDTO.class)).thenReturn(fileRequestDTO);
        when(fileService.downloadFile(fileRequestDTO)).thenThrow(new FileNotFoundInS3Exception("File not found"));

        assertThrows(FileNotFoundInS3Exception.class, () -> {
            fileController.downloadFile(fileRequest);
        });
    }
}
