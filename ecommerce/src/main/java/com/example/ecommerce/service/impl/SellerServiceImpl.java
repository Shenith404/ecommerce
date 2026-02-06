package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.domain.UserRole;
import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.reponse.SellerResponseDTO;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.SellerCreateDTO;
import com.example.ecommerce.dto.request.VerifySellerEmailRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.AddressMapper;
import com.example.ecommerce.mapper.SellerMapper;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.service.interfaces.AddressService;
import com.example.ecommerce.service.interfaces.AuthService;
import com.example.ecommerce.service.interfaces.SellerService;
import com.example.ecommerce.service.interfaces.VerificationCodeService;
import com.example.ecommerce.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final AuthService authService;
    private final VerificationCodeService verificationCodeService;



    @Override
    public AuthResponseDTO sellerLogin(LoginRequestDTO loginRequestDTO) {
        String email=loginRequestDTO.getEmail();
        loginRequestDTO.setEmail(AppUtil.SELLER_PREFIX + email);
        return authService.loginUser(loginRequestDTO).getData();
    }



    @Override
    public SellerResponseDTO findSellerProfile(String token) {
        String email = jwtProvider.getEmailFromToken(token);
        return this.getSellerByEmail(email);
    }

    @Override
    public SellerResponseDTO createSeller(SellerCreateDTO sellerCreateDTO) {
        Seller isExist = sellerRepository.findByEmail(sellerCreateDTO.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller already exists with email: " + sellerCreateDTO.getEmail())
                );

        Seller newSeller = new Seller();
        Address savedAddress = addressService.createAddress(sellerCreateDTO.getAddress());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setEmail(sellerCreateDTO.getEmail());
        newSeller.setSellerName(sellerCreateDTO.getSellerName());
        newSeller.setMobile(sellerCreateDTO.getMobile());
        newSeller.setPassword(passwordEncoder.encode(sellerCreateDTO.getPassword()));
        newSeller.setBusinessDetails(sellerCreateDTO.getBusinessDetails());
        newSeller.setBankDetails(sellerCreateDTO.getBankDetails());
        newSeller.setGSTIN(sellerCreateDTO.getGSTIN());
        newSeller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        newSeller.setRole(UserRole.ROLE_SELLER);
        Seller savedSeller = sellerRepository.save(newSeller);
        LOGGER.info("New seller created with id: {}", savedSeller.getId());

        return SellerMapper.toDto(savedSeller);
    }

    @Override
    public SellerResponseDTO getSellerById(String id) {
        var seller = sellerRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found with id: " + id)
                );
        return SellerMapper.toDto(seller);
    }

    @Override
    public SellerResponseDTO getSellerByEmail(String email) {
        var seller = sellerRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found with email: " + email)
                );
        return SellerMapper.toDto(seller);
    }

    @Override
    public List<SellerResponseDTO> getAllSellers(AccountStatus accountStatus) {
        return List.of();
    }

    @Override
    public SellerResponseDTO updateSellerProfile(UUID sellerId, SellerCreateDTO sellerCreateDTO) {
        Seller existingSeller = sellerRepository.findById(sellerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found with id: " + sellerId)
                );
        if (sellerCreateDTO.getSellerName() != null) {
            existingSeller.setSellerName(sellerCreateDTO.getSellerName());
        }
        if (sellerCreateDTO.getMobile() != null) {
            existingSeller.setMobile(sellerCreateDTO.getMobile());
        }
        if (sellerCreateDTO.getEmail() != null) {
            existingSeller.setEmail(sellerCreateDTO.getEmail());
        }
        if (sellerCreateDTO.getBusinessDetails() != null && sellerCreateDTO.getBusinessDetails().getBusinessName() != null) {
            existingSeller.getBusinessDetails().setBusinessName(sellerCreateDTO.getBusinessDetails().getBusinessName());
        }
        if (sellerCreateDTO.getBankDetails() != null
                && sellerCreateDTO.getBankDetails().getAccountNumber() != null
                && sellerCreateDTO.getBankDetails().getIfscCode() != null
                && sellerCreateDTO.getBankDetails().getAccountHolderName() != null) {

            existingSeller.getBankDetails().setAccountNumber(sellerCreateDTO.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(sellerCreateDTO.getBankDetails().getIfscCode());
            existingSeller.getBankDetails().setAccountHolderName(sellerCreateDTO.getBankDetails().getAccountHolderName());
        }
        if(sellerCreateDTO.getAddress() != null){
            existingSeller.setPickupAddress(AddressMapper.toEntity(sellerCreateDTO.getAddress()));
        }
        if(sellerCreateDTO.getGSTIN() != null){
            existingSeller.setGSTIN(sellerCreateDTO.getGSTIN());
        }
        Seller updatedSeller = sellerRepository.save(existingSeller);
        LOGGER.info("Seller profile updated for id: {}", sellerId);
        return SellerMapper.toDto(updatedSeller);

    }

    @Override
    public void deleteSeller(UUID sellerId) {
        Seller existingSeller = sellerRepository.findById(sellerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found with id: " + sellerId)
                );
        sellerRepository.delete(existingSeller);
        LOGGER.info("Seller deleted with id: {}", sellerId);

    }

    @Override
    public SellerResponseDTO verifySellerEmail(VerifySellerEmailRequestDTO verifySellerEmailRequestDTO) {
        var seller = sellerRepository.findByEmail(verifySellerEmailRequestDTO.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found with email: " + verifySellerEmailRequestDTO.getEmail())
                );
        VerificationCode verificationCode = verificationCodeService.findByEmail(verifySellerEmailRequestDTO.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Verification code not found for email: " + verifySellerEmailRequestDTO.getEmail())
                );
        if(!verificationCode.getOtp().equals(verifySellerEmailRequestDTO.getOtp())){
            throw new ResourceNotFoundException("Invalid OTP for email: " + verifySellerEmailRequestDTO.getEmail());
        }
        seller.setEmailVerified(true);
        Seller updatedSeller = sellerRepository.save(seller);
        LOGGER.info("Seller email verified for email: {}", verifySellerEmailRequestDTO.getEmail());
        return SellerMapper.toDto(updatedSeller);
    }

    @Override
    public SellerResponseDTO updateSellerAccountStatus(UUID sellerId, AccountStatus accountStatus) {
        var seller = sellerRepository.findById(sellerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found")
                );
        seller.setEmailVerified(true);
        Seller updatedSeller = sellerRepository.save(seller);
        LOGGER.info("Seller email verified for id: {}", sellerId);
        return SellerMapper.toDto(updatedSeller);
    }
}
