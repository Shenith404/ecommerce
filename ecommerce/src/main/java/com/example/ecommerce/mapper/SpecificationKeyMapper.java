package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.SpecificationKeyResponseDTO;
import com.example.ecommerce.model.SpecificationKey;

import java.util.stream.Collectors;

public class SpecificationKeyMapper {
    public static SpecificationKeyResponseDTO toDto(SpecificationKey specificationKey) {
        if (specificationKey == null) {
            return null;
        }
        return SpecificationKeyResponseDTO.builder()
                .id(specificationKey.getId().toString())
                .name(specificationKey.getName())
                .isRequired(specificationKey.isRequired())
                .options(specificationKey.getOptions() != null 
                    ? specificationKey.getOptions().stream()
                        .map(SpecificationOptionMapper::toDto)
                        .collect(Collectors.toSet())
                    : null)
                .build();
    }
}
