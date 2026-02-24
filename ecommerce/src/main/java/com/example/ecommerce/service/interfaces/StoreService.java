package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.model.Store;

import java.util.Optional;

public interface StoreService {
    public Optional<Store> getStoreEntityById(String storeId);
}
