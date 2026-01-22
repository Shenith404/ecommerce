package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.dto.reponse.SellerResponseDTO;
import com.example.ecommerce.dto.request.SellerCreateDTO;
import java.util.List;
import java.util.UUID;

public interface SellerService {
    SellerResponseDTO findSellerProfile(String token);
    SellerResponseDTO createSeller(SellerCreateDTO sellerCreateDTO);
    SellerResponseDTO getSellerById(String id);
    SellerResponseDTO getSellerByEmail(String email);
    List<SellerResponseDTO> getAllSellers(AccountStatus accountStatus);
    SellerResponseDTO updateSellerProfile(UUID sellerId, SellerCreateDTO sellerCreateDTO);
    void deleteSeller(UUID sellerId);
    SellerResponseDTO verifySellerEmail(String email, String otp);
    SellerResponseDTO updateSellerAccountStatus(UUID sellerId, AccountStatus accountStatus);

}
