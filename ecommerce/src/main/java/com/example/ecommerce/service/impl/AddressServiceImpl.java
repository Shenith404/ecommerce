package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.AddressCreateRequestDTO;
import com.example.ecommerce.mapper.AddressMapper;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.service.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    @Override
    public Address createAddress(AddressCreateRequestDTO address) {
        return addressRepository.save(AddressMapper.toEntity(address));
    }
}
