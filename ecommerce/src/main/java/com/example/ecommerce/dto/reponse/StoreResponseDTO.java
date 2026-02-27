package com.example.ecommerce.dto.reponse;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDTO {
    private String id;
    private String storeName;
    private String seoSlug;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String returnPolicy;
    private double averageRating;
}
