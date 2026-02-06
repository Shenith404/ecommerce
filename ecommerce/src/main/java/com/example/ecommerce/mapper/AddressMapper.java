package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.AddressCreateRequestDTO;
import com.example.ecommerce.model.Address;

public class AddressMapper {
    public static Address toEntity(AddressCreateRequestDTO addressCreateRequestDTO) {
        return Address.builder()
                .address(addressCreateRequestDTO.getAddress())
                .city(addressCreateRequestDTO.getCity())
                .state(addressCreateRequestDTO.getState())
                .postalCode(addressCreateRequestDTO.getPostalCode())
                .country(addressCreateRequestDTO.getCountry())
                .mobile(addressCreateRequestDTO.getMobile())
                .build();
    }

    public static AddressCreateRequestDTO toDTO(Address address) {
        return new AddressCreateRequestDTO(
                address.getAddress(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.getMobile()
        );
    }
}
