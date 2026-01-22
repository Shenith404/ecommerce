package com.example.ecommerce.dto.reponse;

import com.example.ecommerce.model.Address;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private String id;
    private String email;
    private String fullName;
    private String role;
    private String createdAt;
    private String updatedAt;
    private List<AddressResponseDTO> addresses;
}
