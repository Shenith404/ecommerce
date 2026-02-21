package com.example.ecommerce.dto.reponse;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDTO {
    private String id;
    private String name;
    private String seoSlug;
    private String logoUrl;
}
