package com.filestorage.s3_filestorage_app.service.s3;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.filestorage.s3_filestorage_app.constant.FileServiceConstants;
import com.filestorage.s3_filestorage_app.dto.FileRequestDTO;
import com.filestorage.s3_filestorage_app.dto.FileResponseDTO;
import com.filestorage.s3_filestorage_app.exceptions.S3ServiceException;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@Slf4j
public class S3FileSearchService {

    private final S3Client s3Client;
    
    public S3FileSearchService(S3Client s3Client) {
    	
        this.s3Client = s3Client;
    }

    public FileResponseDTO searchFileInS3(FileRequestDTO fileRequestDTO) {
        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        ArrayList<String> files = new ArrayList<>();

        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(FileServiceConstants.S3_BUCKET_NAME)
                    .prefix(fileRequestDTO.getUserName() + "/")
                    .build();
            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            for (S3Object s3Object : response.contents()) {
                String fileName = s3Object.key().substring(fileRequestDTO.getUserName().length() + 1);
                if (fileName.contains(fileRequestDTO.getFileName())) {
                    files.add(fileName);
                }
            }

            if (files.isEmpty()) {
                fileResponseDTO.setStatus(FileServiceConstants.STATUS_NOT_FOUND);
                fileResponseDTO.setMessage("No matching files found for the filename " + fileRequestDTO.getFileName());
                log.info("No matching files found for the filename " + fileRequestDTO.getFileName());
            } else {
                fileResponseDTO.setStatus(FileServiceConstants.STATUS_SUCCESS);
                fileResponseDTO.setFiles(files);
                fileResponseDTO.setMessage("Files found successfully");
                log.info("Files found successfully");
            }
        } catch (Exception e) {
            log.error("Error occurred while searching for files in S3: ", e);
            throw new S3ServiceException("An error occurred while accessing S3. Please try again later.");
        }

        return fileResponseDTO;
    }
}
