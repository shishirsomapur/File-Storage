package com.filestorage.s3_filestorage_app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;
import com.filestorage.s3_filestorage_app.pojo.FileRequest;
import com.filestorage.s3_filestorage_app.pojo.FileResponse;
import com.filestorage.s3_filestorage_app.service.interfaces.FileService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/file")
public class FileController {

	FileService fileService;

	ModelMapper modelMapper;

	public FileController(FileService fileService,
			ModelMapper modelMapper) { 

		this.fileService = fileService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public FileResponse searchFile(@Valid @RequestBody FileRequest fileRequest) {

		log.info("Request recevied to search a file: " + fileRequest);

		log.info("Sending the request to FileService to search for the file");

		FileRequestDTO fileRequestDTO = modelMapper.map(fileRequest, FileRequestDTO.class);

		FileResponseDTO fileResponseDTO =  fileService.searchFile(fileRequestDTO);

		log.info("Response received from FileService: " + fileResponseDTO);

		FileResponse fileResponse = modelMapper.map(fileResponseDTO, FileResponse.class);
		
		log.info("Returning the response");

		return fileResponse;

	}

	@GetMapping("/download")
	public ResponseEntity<InputStreamResource> downloadFile(@Valid @RequestBody FileRequest fileRequest) {

		log.info("Request received to download file: " + fileRequest);

		log.info("Sending the request to FileService to download file");

		FileRequestDTO fileRequestDTO = modelMapper.map(fileRequest, FileRequestDTO.class);
		
		InputStreamResource resource = fileService.downloadFile(fileRequestDTO);
		
		log.info("Response received from FileService: " + resource);
		
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=\"" + fileRequest.getFileName() + "\"" )
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

}
