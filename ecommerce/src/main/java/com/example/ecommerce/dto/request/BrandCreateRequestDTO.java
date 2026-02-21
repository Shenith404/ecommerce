package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandCreateRequestDTO {
    @NotBlank(message = "Brand name is required")
    private String name;
}
