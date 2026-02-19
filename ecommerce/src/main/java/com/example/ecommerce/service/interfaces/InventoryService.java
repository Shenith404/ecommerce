package com.example.ecommerce.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

public interface InventoryService {
    void increaseStock(String variantId, int amount);

    void decreaseStock(String variantId, int amount);
}
