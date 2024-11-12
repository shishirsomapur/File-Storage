package com.filestorage.s3_filestorage_app.service.impl;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;
import com.filestorage.s3_filestorage_app.service.interfaces.FileService;
import com.filestorage.s3_filestorage_app.service.s3.S3FileDownloadService;
import com.filestorage.s3_filestorage_app.service.s3.S3FileSearchService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final S3FileSearchService fileSearchService;
    
    private final S3FileDownloadService fileDownloadService;

    public FileServiceImpl(S3FileSearchService fileSearchService, S3FileDownloadService fileDownloadService) {
    	
        this.fileSearchService = fileSearchService;
        this.fileDownloadService = fileDownloadService;
    }

    @Override
    public FileResponseDTO searchFile(FileRequestDTO fileRequestDTO) {
    	
        log.info("Request received from FileController to search for the file: " + fileRequestDTO);
        
        FileResponseDTO response = fileSearchService.searchFileInS3(fileRequestDTO);
        
        log.info("Response prepared " + response);
        
        log.info("Sending response to FileController");
        
        return response;
    }

    @Override
    public InputStreamResource downloadFile(FileRequestDTO fileRequestDTO) {
    	
        log.info("Request received from FileController to download file: " + fileRequestDTO);
        
        InputStreamResource resource = fileDownloadService.downloadFileFromS3(fileRequestDTO);
        
        log.info("Sending the response to the fileController");
        
        return resource;
    }
}
