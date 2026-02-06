package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.request.AddressCreateRequestDTO;
import com.example.ecommerce.model.Address;

public interface AddressService {
    Address createAddress(AddressCreateRequestDTO address);
}
