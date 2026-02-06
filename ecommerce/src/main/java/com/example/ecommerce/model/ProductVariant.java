package com.example.ecommerce.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String sku; // Unique Stock Keeping Unit (e.g., TS-RED-LGE)

    private String color;

    private String size;

    @Column(nullable = false)
    private int stockQuantity;

    // Use BigDecimal for Money in Production!
    @Column(nullable = false)
    private BigDecimal mrpPrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    private double discountPercentage;

    private String imageUrl;
}
