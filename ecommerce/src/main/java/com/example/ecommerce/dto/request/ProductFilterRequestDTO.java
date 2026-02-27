package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequestDTO {

    // --- Text Search ---
    private String search;

    // --- Category ---
    private String categorySlug;

    // --- Brands (multi-select) ---
    private List<String> brandSlugs;

    // --- Price Range (from ProductVariant.sellingPrice) ---
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    // --- Item Condition (multi-select: NEW, USED, REFURBISHED) ---
    private List<String> itemConditions;

    // --- Rating ---
    @Min(value = 0) @Max(value = 5)
    private Double minRating;

    // --- Stock ---
    private boolean inStockOnly;

    // --- Discount ---
    @Min(0) @Max(100)
    private Double minDiscount;

    // --- Dynamic Specifications (e.g., color=Black, storage=128GB) ---
    private Map<String, String> specifications;

    // --- Pagination ---
    @Min(0)
    @Builder.Default
    private int page = 0;

    @Min(1) @Max(50)
    @Builder.Default
    private int size = 20;

    // --- Sorting ---
    // Allowed: sellingPrice, averageRating, discountPercentage, createdAt, title
    @Builder.Default
    private String sortBy = "createdAt";

    // asc | desc
    @Builder.Default
    private String sortDir = "desc";
}

