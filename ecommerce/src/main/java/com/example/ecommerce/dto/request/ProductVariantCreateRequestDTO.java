package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantCreateRequestDTO {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "SKU is required")
    private String sku; // Unique Stock Keeping Unit (e.g., TS-RED-LGE)

    private String color;

    private String size;

    @Min(value = 0, message = "Stock Quantity must be zero or positive")
    private int stockQuantity = 0;

    @NotNull(message = "MRP Price is mandatory")
    @Positive(message = "MRP Price must be positive")
    private BigDecimal mrpPrice;

    @NotNull(message = "Selling Price is mandatory")
    @PositiveOrZero(message = "Selling Price must be zero or positive")
    private BigDecimal sellingPrice;

    private Map<String, String> specifications ;

}
