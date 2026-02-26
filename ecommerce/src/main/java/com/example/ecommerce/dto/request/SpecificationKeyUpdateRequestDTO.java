package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecificationKeyUpdateRequestDTO {
    @NotBlank(message = "Specification key id is required")
    private String id;
    
    @NotBlank(message = "Specification key name is required")
    private String name;
    
    private boolean isRequired = false;
}
