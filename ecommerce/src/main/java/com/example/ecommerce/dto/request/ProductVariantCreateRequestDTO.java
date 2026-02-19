package com.example.ecommerce.dto.request;

import com.example.ecommerce.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantCreateRequestDTO {

    @NotBlank(message = "Product ID is required")
    private String productId;


    private String sku; // Unique Stock Keeping Unit (e.g., TS-RED-LGE)

    private String color;

    private String size;

    private int stockQuantity = 0;

    private BigDecimal mrpPrice;

    private BigDecimal sellingPrice;

}
