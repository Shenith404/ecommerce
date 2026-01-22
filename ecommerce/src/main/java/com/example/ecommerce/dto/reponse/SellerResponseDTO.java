package com.example.ecommerce.dto.reponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerResponseDTO {
    private String id;
    private String sellerName;
    private String mobile;
    private String email;
    private String GSTIN;
    private String role;
    private boolean isEmailVerified;
    private String accountStatus;
    private String createdAt;
    private String updatedAt;

    // Business Details
    private String businessName;
    private String businessEmail;
    private String businessMobile;
    private String businessAddress;
    private String logo;
    private String banner;

    // Bank Details
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;

    // Pickup Address
    private AddressResponseDTO pickupAddress;
}
