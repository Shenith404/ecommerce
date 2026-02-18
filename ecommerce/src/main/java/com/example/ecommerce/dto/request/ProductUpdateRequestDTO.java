package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDTO {
    @NotBlank(message = "Product ID is mandatory")
    private String id;
    @Size(max = 100,min = 3,message = "Title cannot exceed 100 characters and must be at least 3 characters long")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String  categoryId;

}
