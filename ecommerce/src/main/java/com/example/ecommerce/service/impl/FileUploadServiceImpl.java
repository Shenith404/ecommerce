package com.example.ecommerce.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.service.interfaces.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/svg+xml"
    );
    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "webp", "svg"
    );

    private final Cloudinary cloudinary;

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed limit of 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Invalid file type. Only image files are allowed (JPEG, PNG, GIF, WebP, SVG)"
            );
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException(
                        "Invalid file extension. Allowed extensions: jpg, jpeg, png, gif, webp, svg"
                );
            }
        }
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);

        // Upload to a specific folder in Cloudinary to keep it organized
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "ecommerce/products",
                        "format", "webp", // Forces Cloudinary to convert it to WebP
                        "quality", "80" // Let Cloudinary optimize the quality for you
                ));

        // secure_url ensures you get an HTTPS link back
        return uploadResult.get("secure_url").toString();
    }

    @Override
    public void deleteImage(String imageUrl) throws IOException {
            // Extract public ID from the URL
            String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
            cloudinary.uploader().destroy("ecommerce/products/" + publicId, ObjectUtils.emptyMap());
    }
}
