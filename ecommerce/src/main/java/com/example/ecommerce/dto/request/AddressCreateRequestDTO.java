package com.example.ecommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateRequestDTO {
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String mobile;
}
