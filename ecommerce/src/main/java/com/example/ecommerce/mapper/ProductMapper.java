package com.example.ecommerce.mapper;

import com.example.ecommerce.domain.ItemCondition;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.model.Product;

import java.util.ArrayList;

public class ProductMapper {
    public static ProductResponseDTO toDto(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId().toString())
                .slug(product.getSlug())
                .title(product.getTitle())
                .description(product.getDescription())
                .numRatings(product.getNumRatings())
                .averageRating(product.getAverageRating())
                .itemCondition(product.getItemCondition().name())
                .specifications(product.getSpecifications())
                .productVariants(product.getProductVariants() !=null ? product.getProductVariants().stream().map(ProductVariantMapper::toDto).toList() : new ArrayList<>())
                .reviews(product.getReviews() != null ?  product.getReviews().stream().map(ReviewMapper::toDto).toList() : new ArrayList<>())
                .build();
    }
    public static Product toEntity(ProductCreateRequestDTO productCreateRequestDTO) {
        return Product.builder()
                .title(productCreateRequestDTO.getTitle())
                .description(productCreateRequestDTO.getDescription())
                .itemCondition(ItemCondition.valueOf(productCreateRequestDTO.getItemCondition()))
                .specifications(productCreateRequestDTO.getSpecifications())
                .build();
    }
}
