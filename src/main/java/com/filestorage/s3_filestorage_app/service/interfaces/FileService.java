package com.filestorage.s3_filestorage_app.service.interfaces;

import org.springframework.core.io.InputStreamResource;

import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;

public interface FileService {
	
	FileResponseDTO searchFile(FileRequestDTO fileRequestDTO);
	
	InputStreamResource downloadFile(FileRequestDTO fileRequestDTO);
	
}
