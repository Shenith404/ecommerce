package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.ProductVariantResponseDTO;
import com.example.ecommerce.dto.request.ProductVariantCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductVariantUpdateRequestDTO;
import com.example.ecommerce.model.ProductVariant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductVariantService {
    ProductVariantResponseDTO createVariant(ProductVariantCreateRequestDTO dto, MultipartFile image) throws IOException;
    ProductVariantResponseDTO update(String id, ProductVariantUpdateRequestDTO dto);
    ProductVariantResponseDTO updateImage(String id, MultipartFile image) throws IOException;
    void delete(String id);
    ProductVariantResponseDTO getById(String id);
    Optional<ProductVariant> getEntityById(String id);
    List<ProductVariantResponseDTO> getAll(String productId);
}
