package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Store;
import com.example.ecommerce.repository.StoreRepository;
import com.example.ecommerce.service.interfaces.StoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {


    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    @Override
    public Optional<Store> getStoreEntityById(String storeId) {
        return storeRepository.findById(UUID.fromString(storeId));
    }



}
