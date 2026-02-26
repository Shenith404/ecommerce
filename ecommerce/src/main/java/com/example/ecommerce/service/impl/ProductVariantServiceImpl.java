package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.dto.reponse.ProductVariantResponseDTO;
import com.example.ecommerce.dto.request.ProductVariantCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductVariantUpdateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProductVariantMapper;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ProductVariant;
import com.example.ecommerce.repository.ProductVariantRepository;
import com.example.ecommerce.service.interfaces.FileUploadService;
import com.example.ecommerce.service.interfaces.ProductService;
import com.example.ecommerce.service.interfaces.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductVariantServiceImpl.class);

    private final ProductVariantRepository variantRepository;
    private final ProductService productService;
    private final FileUploadService fileUploadService;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ProductVariantResponseDTO createVariant(ProductVariantCreateRequestDTO dto, MultipartFile image) throws IOException {

        Product product = productService.getEntityById(dto.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // 1. Upload Image to Cloudinary
        String imageUrl = fileUploadService.uploadImage(image);

        // 2. Calculate Discount Percentage
        double discount = 0.0;
        if (dto.getMrpPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savings = dto.getMrpPrice().subtract(dto.getSellingPrice());
            discount = savings.divide(dto.getMrpPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
        }

        // 3. Build and Save Entity
        ProductVariant variant = ProductVariantMapper.toEntity(dto);
        variant.setProduct(product);
        variant.setImageUrl(imageUrl);
        variant.setDiscountPercentage(discount);

        var createdVariant = variantRepository.save(variant);
        LOGGER.info("Created Product Variant with ID: {}", createdVariant.getId());
        return ProductVariantMapper.toDto(createdVariant);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ProductVariantResponseDTO update(String id, ProductVariantUpdateRequestDTO dto) {
        ProductVariant variant = variantRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Variant not found"));

        //check owner
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!variant.getProduct().getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this variant");
        }

        if (dto.getSellingPrice().compareTo(dto.getMrpPrice()) > 0) {
            throw new IllegalArgumentException("Selling price cannot exceed MRP");
        }

        variant.setMrpPrice(dto.getMrpPrice());
        variant.setSellingPrice(dto.getSellingPrice());

        // Recalculate discount
        if (dto.getMrpPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savings = dto.getMrpPrice().subtract(dto.getSellingPrice());
            double discount = savings.divide(dto.getMrpPrice(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
            variant.setDiscountPercentage(discount);
        }

        var updatedVariant = variantRepository.save(variant);
        LOGGER.info("Updated Product Variant with ID: {}", updatedVariant.getId());
        return ProductVariantMapper.toDto(updatedVariant);
    }
    @Transactional
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Override
    public ProductVariantResponseDTO updateImage(String id, MultipartFile image) throws IOException {
        ProductVariant variant = variantRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Variant not found"));

        //check owner
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!variant.getProduct().getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this variant");
        }

        //delete old image from cloudinary
        if (variant.getImageUrl() != null) {
            fileUploadService.deleteImage(variant.getImageUrl());
        }

        // Upload new image
        String imageUrl = fileUploadService.uploadImage(image);
        variant.setImageUrl(imageUrl);

        var updatedVariant = variantRepository.save(variant);
        LOGGER.info("Updated Image for Product Variant with ID: {}", updatedVariant.getId());
        return ProductVariantMapper.toDto(updatedVariant);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(String id) {
        ProductVariant variant = variantRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!variant.getProduct().getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to delete this variant");
        }
        variantRepository.delete(variant);
        LOGGER.info("Deleted Product Variant with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponseDTO getById(String id) {
        ProductVariant variant = variantRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
        return ProductVariantMapper.toDto(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariant> getEntityById(String id) {
        return variantRepository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponseDTO> getAll(String productId) {
        List<ProductVariant> variants = variantRepository.findByProductId(UUID.fromString(productId));
        return variants.stream().map(ProductVariantMapper::toDto).toList();
    }
}