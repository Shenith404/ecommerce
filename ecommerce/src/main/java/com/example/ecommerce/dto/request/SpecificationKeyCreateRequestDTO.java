package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecificationKeyCreateRequestDTO {
    @NotBlank(message = "Specification key name is required")
    private String name;
    
    private boolean isRequired = false;
}
