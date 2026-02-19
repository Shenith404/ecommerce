package com.example.ecommerce.service.impl;
import com.example.ecommerce.dto.reponse.ProductVariantResponseDTO;
import com.example.ecommerce.dto.request.ProductVariantCreateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProductVariantMapper;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ProductVariant;
import com.example.ecommerce.repository.ProductVariantRepository;
import com.example.ecommerce.service.interfaces.FileUploadService;
import com.example.ecommerce.service.interfaces.ProductService;
import com.example.ecommerce.service.interfaces.ProductVariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductVariantServiceImpl.class);

    private final ProductVariantRepository variantRepository;
    private final ProductService productService;
    private final FileUploadService fileUploadService;

    @Transactional
    @Override
    public ProductVariantResponseDTO createVariant(ProductVariantCreateRequestDTO dto, MultipartFile image) throws IOException {

        Product product = productService.getEntityById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // 1. Upload Image to Cloudinary
        String imageUrl = fileUploadService.uploadImage(image);

        // 2. Calculate Discount Percentage
        double discount = 0.0;
        if (dto.getMrpPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savings = dto.getMrpPrice().subtract(dto.getSellingPrice());
            discount = savings.divide(dto.getMrpPrice(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
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
}