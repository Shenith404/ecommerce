package com.example.ecommerce.dto.reponse;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponseDTO {
    private String id;

    private String sku; // Unique Stock Keeping Unit (e.g., TS-RED-LGE)

    private String color;

    private String size;

    private int stockQuantity;
    // Use BigDecimal for Money in Production!
    private BigDecimal mrpPrice;

    private BigDecimal sellingPrice;

    private double discountPercentage;

    private String imageUrl;
}
