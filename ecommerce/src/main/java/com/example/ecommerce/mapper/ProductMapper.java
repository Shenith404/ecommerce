package com.example.ecommerce.mapper;

import com.example.ecommerce.domain.ItemCondition;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.model.Product;

import java.util.ArrayList;

public class ProductMapper {

    private static final int META_DESCRIPTION_MAX_LENGTH = 160;
    private static final String CANONICAL_BASE = "/products/slug/";

    public static ProductResponseDTO toDto(Product product) {

        String categoryName = (product.getCategory() != null) ? product.getCategory().getName() : null;
        String categorySlug = (product.getCategory() != null) ? product.getCategory().getSlug() : null;

        String brandName = (product.getBrand() != null) ? product.getBrand().getName() : null;
        String brandSlug = (product.getBrand() != null) ? product.getBrand().getSeoSlug() : null;

        String canonicalUrl = CANONICAL_BASE + product.getSlug();

        String metaTitle = (categoryName != null)
                ? product.getTitle() + " | " + categoryName
                : product.getTitle();

        String metaDescription = null;
        if (product.getDescription() != null) {
            metaDescription = product.getDescription().length() > META_DESCRIPTION_MAX_LENGTH
                    ? product.getDescription().substring(0, META_DESCRIPTION_MAX_LENGTH)
                    : product.getDescription();
        }

        return ProductResponseDTO.builder()
                .id(product.getId().toString())
                .slug(product.getSlug())
                .title(product.getTitle())
                .description(product.getDescription())
                .numRatings(product.getNumRatings())
                .averageRating(product.getAverageRating())
                .itemCondition(product.getItemCondition().name())
                .specifications(product.getSpecifications())
                .productVariants(product.getProductVariants() != null
                        ? product.getProductVariants().stream().map(ProductVariantMapper::toDto).toList()
                        : new ArrayList<>())
                .reviews(product.getReviews() != null
                        ? product.getReviews().stream().map(ReviewMapper::toDto).toList()
                        : new ArrayList<>())
                .categoryName(categoryName)
                .categorySlug(categorySlug)
                .brandName(brandName)
                .brandSlug(brandSlug)
                .canonicalUrl(canonicalUrl)
                .metaTitle(metaTitle)
                .metaDescription(metaDescription)
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


