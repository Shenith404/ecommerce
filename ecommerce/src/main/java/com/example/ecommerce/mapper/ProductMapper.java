package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.model.Product;

public class ProductMapper {
    public static ProductResponseDTO toDto(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId().toString())
                .title(product.getTitle())
                .description(product.getDescription())
                .numRatings(product.getNumRatings())
                .averageRating(product.getAverageRating())
                .productVariants(product.getProductVariants().stream().map(ProductVariantMapper::toDto).toList())
                .reviews(product.getReviews().stream().map(ReviewMapper::toDto).toList())
                .build();
    }
    public static Product toEntity(ProductCreateRequestDTO productCreateRequestDTO) {
        return Product.builder()
                .title(productCreateRequestDTO.getTitle())
                .description(productCreateRequestDTO.getDescription())
                .build();
    }
}
