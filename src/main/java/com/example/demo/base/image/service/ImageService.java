package com.example.demo.base.image.service;

import com.example.demo.base.s3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Service amazonS3Service;

    public String upload(MultipartFile file, String name) {

        String url = amazonS3Service.imageUpload(file, name);

        return url;
    }
}