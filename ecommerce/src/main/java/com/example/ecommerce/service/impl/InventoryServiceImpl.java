package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements com.example.ecommerce.service.interfaces.InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final ProductVariantRepository variantRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void increaseStock(String variantId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        // ownership check
        var variant = variantRepository.findById(UUID.fromString(variantId))
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + variantId));
        String sellerEmail= jwtProvider.getEmailFromHeader();
        if (!variant.getProduct().getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("You do not have permission to modify this product variant's stock.");
        }

        variantRepository.addStock(UUID.fromString(variantId), amount);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void decreaseStock(String variantId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        // ownership check
        var variant = variantRepository.findById(UUID.fromString(variantId))
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + variantId));
        String sellerEmail= jwtProvider.getEmailFromHeader();
        if (!variant.getProduct().getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("You do not have permission to modify this product variant's stock.");
        }

        // The query returns how many rows were updated.
        // If it returns 0, it means the "v.stockQuantity >= :amount" rule failed.
        int updatedRows = variantRepository.removeStock(UUID.fromString(variantId), amount);

        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Insufficient stock to complete this operation! Cannot sell more than we have.");
        }
    }

}
