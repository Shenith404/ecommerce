package com.example.ecommerce.dto.reponse;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationKeyResponseDTO {
    private String id;
    private String name;
    private boolean isRequired;
    @Builder.Default
    private Set<SpecificationOptionResponseDTO> options = new HashSet<>();
}
