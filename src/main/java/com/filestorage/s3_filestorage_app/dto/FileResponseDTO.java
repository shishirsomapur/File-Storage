package com.filestorage.s3_filestorage_app.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDTO {
	
	private String status;
	
	private ArrayList <String> files;
	
	private String message;

}
