package com.example.ecommerce.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private String id;
    private String slug;
    private String title;
    private String description;
    private String itemCondition;
    private Map<String ,String> specifications;
    private int numRatings;
    private double averageRating;
    private List<ProductVariantResponseDTO> productVariants;
    private List<ReviewResponseDTO> reviews;
    private String categoryName;
    private String categorySlug;
    private String brandName;
    private String brandSlug;
    /** Canonical URL for SEO — e.g. /products/slug/apple-iphone-15-pro */
    private String canonicalUrl;
    /** For <title> tag — e.g. "Apple iPhone 15 Pro | Smartphones" */
    private String metaTitle;
    /** For <meta name="description"> — first 160 chars of description */
    private String metaDescription;
}
