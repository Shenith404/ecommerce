package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.model.Brand;

public class BrandMapper {
    public static BrandResponseDTO toDto(Brand brand) {
        return BrandResponseDTO.builder()
                .id(brand.getId().toString())
                .name(brand.getName())
                .seoSlug(brand.getSeoSlug())
                .logoUrl(brand.getLogoUrl())
                .build();
    }
}
