package com.filestorage.s3_filestorage_app.pojo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
	
	private String status;
	
	private ArrayList <String> files;
	
	private String message;

}
