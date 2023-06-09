package com.example.demo.base.s3.repository;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class AmazonS3Repository {
    private final AmazonS3 amazonS3;

    public PutObjectResult upload(String bucketName, String objectName, MultipartFile file, String contentType) {

        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(contentType);

            return amazonS3.putObject(bucketName, objectName, file.getInputStream(), metadata);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getFile(String bucketName, String objectName) {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, objectName);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
                outputStream.write(bytesArray, 0, bytesRead);
            }

            outputStream.close();
            s3ObjectInputStream.close();

            System.out.format("Object %s has been downloaded.\n", objectName);

            return outputStream.toByteArray();
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw e; // 예외를 던져서 호출한 곳에서 처리하도록 함
        } catch (SdkClientException e) {
            e.printStackTrace();
            throw e; // 예외를 던져서 호출한 곳에서 처리하도록 함
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
