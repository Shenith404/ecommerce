package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.model.Seller;

/**
 * Service for verifying seller authorization and ownership.
 * This service provides production-level authorization checks for seller operations.
 */
public interface SellerVerificationService {

    /**
     * Verifies that the authenticated seller exists and is in good standing.
     * Checks that the seller account is active and verified.
     *
     * @return The verified Seller entity
     * @throws com.example.ecommerce.exception.UnauthorizedAccessException if seller is not authorized
     * @throws com.example.ecommerce.exception.ResourceNotFoundException if seller is not found
     */
    Seller verifyAuthenticatedSeller();

    /**
     * Gets the email of the currently authenticated user from SecurityContext.
     *
     * @return The email of the authenticated user
     * @throws com.example.ecommerce.exception.UnauthorizedAccessException if no user is authenticated
     */
    String getAuthenticatedUserEmail();
}

