package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.ProductVariantResponseDTO;
import com.example.ecommerce.dto.request.ProductVariantCreateRequestDTO;
import com.example.ecommerce.model.ProductVariant;
import com.example.ecommerce.model.Review;

public class ProductVariantMapper {
    public static ProductVariantResponseDTO toDto(ProductVariant productVariant) {
        return ProductVariantResponseDTO.builder()
                .id(productVariant.getId().toString())
                .sku(productVariant.getSku())
                .color(productVariant.getColor())
                .size(productVariant.getSize())
                .discountPercentage(productVariant.getDiscountPercentage())
                .stockQuantity(productVariant.getStockQuantity())
                .mrpPrice(productVariant.getMrpPrice())
                .sellingPrice(productVariant.getSellingPrice())
                .imageUrl(productVariant.getImageUrl())
                .build();

    }
    public static ProductVariant toEntity(ProductVariantCreateRequestDTO dto) {
        return ProductVariant.builder()
                .sku(dto.getSku())
                .color(dto.getColor())
                .size(dto.getSize())
                .stockQuantity(dto.getStockQuantity())
                .mrpPrice(dto.getMrpPrice())
                .sellingPrice(dto.getSellingPrice())
                .build();
    }

}
