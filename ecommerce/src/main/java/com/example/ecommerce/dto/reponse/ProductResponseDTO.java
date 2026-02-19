package com.example.ecommerce.dto.reponse;

import com.example.ecommerce.model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private int numRatings;
    private double averageRating;
    private List<ProductVariantResponseDTO> productVariants;
    private List<ReviewResponseDTO> reviews;
}
