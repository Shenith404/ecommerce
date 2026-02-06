package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.reponse.SellerResponseDTO;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.SellerCreateDTO;
import com.example.ecommerce.dto.request.VerificationCodeRequestDTO;
import com.example.ecommerce.dto.request.VerifySellerEmailRequestDTO;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    AuthResponseDTO sellerLogin(LoginRequestDTO loginRequestDTO);
    SellerResponseDTO findSellerProfile(String token);
    SellerResponseDTO createSeller(SellerCreateDTO sellerCreateDTO);
    SellerResponseDTO getSellerById(String id);
    SellerResponseDTO getSellerByEmail(String email);
    List<SellerResponseDTO> getAllSellers(AccountStatus accountStatus);
    SellerResponseDTO updateSellerProfile(UUID sellerId, SellerCreateDTO sellerCreateDTO);
    void deleteSeller(UUID sellerId);
    SellerResponseDTO verifySellerEmail(VerifySellerEmailRequestDTO verifySellerEmailRequestDTO);
    SellerResponseDTO updateSellerAccountStatus(UUID sellerId, AccountStatus accountStatus);

}
