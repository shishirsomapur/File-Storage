package com.filestorage.s3_filestorage_app.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRequest {
	
	@NotNull(message = "userName is required")
	private String userName;

	@NotNull(message = "fileName is required")
	private String fileName;
}
