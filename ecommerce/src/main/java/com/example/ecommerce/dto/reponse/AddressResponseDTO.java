package com.example.ecommerce.dto.reponse;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDTO {
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String mobile;
}
