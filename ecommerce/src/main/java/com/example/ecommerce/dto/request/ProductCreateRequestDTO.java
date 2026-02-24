package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequestDTO {
    @NotBlank(message = "Title is mandatory")
    @Size(max = 100,min = 3,message = "Title cannot exceed 100 characters and must be at least 3 characters long")
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Category ID is mandatory")
    private String categoryId;

    @NotBlank(message = "Item condition is mandatory")
    @Pattern(regexp = "New|Used|Refurbished", message = "Item condition must be one of: New, Used, Refurbished")
    private String itemCondition;

    private String brandId;

    private String storeId;

    private Map<String, String> specifications;

}
