package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.AddressResponseDTO;
import com.example.ecommerce.dto.reponse.SellerResponseDTO;
import com.example.ecommerce.model.Seller;

public class SellerMapper {

    public static SellerResponseDTO toDto(Seller seller) {
        return SellerResponseDTO.builder()
                .id(seller.getId().toString())
                .sellerName(seller.getSellerName())
                .mobile(seller.getMobile())
                .email(seller.getEmail())
                .GSTIN(seller.getGSTIN())
                .role(seller.getRole().name())
                .isEmailVerified(seller.isEmailVerified())
                .accountStatus(seller.getAccountStatus().name())
                .createdAt(seller.getCreatedAt().toString())
                .updatedAt(seller.getUpdatedAt().toString())
                // Business Details
                .businessName(seller.getBusinessDetails() != null ? seller.getBusinessDetails().getBusinessName() : null)
                .businessEmail(seller.getBusinessDetails() != null ? seller.getBusinessDetails().getBusinessEmail() : null)
                .businessMobile(seller.getBusinessDetails() != null ? seller.getBusinessDetails().getBusinessMobile() : null)
                .businessAddress(seller.getBusinessDetails() != null ? seller.getBusinessDetails().getBusinessAddress() : null)
                .logo(seller.getBusinessDetails() != null ? seller.getBusinessDetails().getLogo() : null)
                .banner(seller.getBusinessDetails() != null ? seller.getBusinessDetails().getBanner() : null)
                // Bank Details
                .accountHolderName(seller.getBankDetails() != null ? seller.getBankDetails().getAccountHolderName() : null)
                .accountNumber(seller.getBankDetails() != null ? seller.getBankDetails().getAccountNumber() : null)
                .ifscCode(seller.getBankDetails() != null ? seller.getBankDetails().getIfscCode() : null)
                // Pickup Address
                .pickupAddress(seller.getPickupAddress() != null ? AddressResponseDTO.builder()
                        .address(seller.getPickupAddress().getAddress())
                        .city(seller.getPickupAddress().getCity())
                        .state(seller.getPickupAddress().getState())
                        .postalCode(seller.getPickupAddress().getPostalCode())
                        .country(seller.getPickupAddress().getCountry())
                        .mobile(seller.getPickupAddress().getMobile())
                        .build() : null)
                .build();
    }

}
