package com.example.ecommerce.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandFacetDTO {
    private String id;
    private String name;
    private String slug;
    private long count;
}

