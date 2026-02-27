package com.example.ecommerce.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_variant_sku", columnList = "sku", unique = true),
        @Index(name = "idx_variant_selling_price", columnList = "selling_price"),
        @Index(name = "idx_variant_stock", columnList = "stock_quantity"),
        @Index(name = "idx_variant_product_id", columnList = "product_id")
})
public class ProductVariant extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String sku; // Unique Stock Keeping Unit (e.g., TS-RED-LGE)

    @Column(nullable = false)
    private int stockQuantity;

    // Use BigDecimal for Money in Production!
    @Column(nullable = false)
    private BigDecimal mrpPrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    private double discountPercentage;

    private String imageUrl;

    private boolean isDefault= false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,String> specifications = new HashMap<>();
}
