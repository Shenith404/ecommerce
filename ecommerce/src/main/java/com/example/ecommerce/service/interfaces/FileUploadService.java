package com.example.ecommerce.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadImage(MultipartFile file) throws IOException;

    void deleteImage(String imageUrl) throws IOException;
}
