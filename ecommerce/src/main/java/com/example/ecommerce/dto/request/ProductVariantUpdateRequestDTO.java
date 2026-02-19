package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantUpdateRequestDTO {

    private String color;

    private String size;

    @NotNull(message = "MRP Price is mandatory")
    @Positive(message = "MRP Price must be positive")
    private BigDecimal mrpPrice;

    @NotNull(message = "Selling Price is mandatory")
    @PositiveOrZero(message = "Selling Price must be zero or positive")
    private BigDecimal sellingPrice;
}
