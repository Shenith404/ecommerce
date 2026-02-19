package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.ProductVariantResponseDTO;
import com.example.ecommerce.dto.request.ProductVariantCreateRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductVariantService {
    @Transactional
    ProductVariantResponseDTO createVariant(ProductVariantCreateRequestDTO dto, MultipartFile image) throws IOException;
}
