package com.example.ecommerce.dto.reponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    private String token;
    private String role;
}
