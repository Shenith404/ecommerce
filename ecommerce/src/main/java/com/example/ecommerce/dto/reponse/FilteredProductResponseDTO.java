package com.example.ecommerce.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilteredProductResponseDTO {

    private List<ProductResponseDTO> products;
    private int page;
    private int totalPages;
    private long totalElements;

    // --- Live Sidebar Facets (update on every filter request) ---

    /** Available brands within current filter context, with product counts */
    private List<BrandFacetDTO> availableBrands;

    /** Price range of matching products */
    private PriceRangeDTO priceRange;

    /** Available item conditions within current filter context */
    private List<String> availableConditions;

    /** Available rating buckets (5, 4, 3, 2, 1) with counts */
    private Map<Integer, Long> ratingCounts;

    /**
     * Available specification values within current filter context.
     * e.g. { "color": ["Black","White","Red"], "storage": ["64GB","128GB"] }
     */
    private Map<String, List<String>> availableSpecifications;
}

