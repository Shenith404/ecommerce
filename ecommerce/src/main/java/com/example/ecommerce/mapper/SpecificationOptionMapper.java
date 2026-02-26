package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.SpecificationOptionResponseDTO;
import com.example.ecommerce.model.SpecificationOption;

public class SpecificationOptionMapper {
    public static SpecificationOptionResponseDTO toDto(SpecificationOption option) {
        if (option == null) {
            return null;
        }
        return SpecificationOptionResponseDTO.builder()
                .id(option.getId() != null ? option.getId().toString() : null)
                .value(option.getValue())
                .build();
    }
}
