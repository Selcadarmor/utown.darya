package com.example.Utown.service;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.exception.S3UploadException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@AllArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private  final AwsProperties awsProperties;

    public String uploadFile(MultipartFile file, String key) {
        try {
            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(awsProperties.getS3Bucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return  key;
        } catch (IOException e) {
            throw  new S3UploadException("Failed to upload file to S3 storage.", e);
        }
    }

    public byte[] downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsProperties.getS3Bucket())
                .key(key)
                .build();
        try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest)) {
            return  response.readAllBytes();
        } catch (IOException e) {
            throw  new S3UploadException("Failed to download file from S3 storage.", e);
        }
    }
}
