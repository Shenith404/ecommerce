package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecificationOptionCreateRequestDTO {
    @NotBlank(message = "Specification option value is required")
    private String value;
}
