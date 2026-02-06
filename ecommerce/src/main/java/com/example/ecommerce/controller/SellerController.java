package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.VerifySellerEmailRequestDTO;
import com.example.ecommerce.service.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> sellerLogin(LoginRequestDTO loginRequestDTO){
        AuthResponseDTO authResponseDTO = sellerService.sellerLogin(loginRequestDTO);
        ApiResponseDTO<AuthResponseDTO> responseDTO = ApiResponseDTO.<AuthResponseDTO>builder()
                .data(authResponseDTO)
                .message("Seller logged in successfully")
                .build();
        return ResponseEntity.ok(responseDTO);
    }
    @PatchMapping("verify/{otp}")
    public ResponseEntity<ApiResponseDTO<Void>> verifySellerOtp(@Valid @RequestBody VerifySellerEmailRequestDTO requestDTO){
        sellerService.verifySellerEmail(requestDTO);
        ApiResponseDTO<Void> apiResponseDTO = ApiResponseDTO.<Void>builder()
                .data(null)
                .message("Seller OTP verification successful")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }


}
