package com.example.ecommerce.controller;

import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.reponse.SellerResponseDTO;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.SellerCreateDTO;
import com.example.ecommerce.dto.request.VerifySellerEmailRequestDTO;
import com.example.ecommerce.service.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> sellerLogin(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        AuthResponseDTO authResponseDTO = sellerService.sellerLogin(loginRequestDTO);
        ApiResponseDTO<AuthResponseDTO> responseDTO = ApiResponseDTO.<AuthResponseDTO>builder()
                .data(authResponseDTO)
                .message("Seller logged in successfully")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> verifySellerOtp(@Valid @RequestBody VerifySellerEmailRequestDTO requestDTO){
        SellerResponseDTO sellerResponseDTO = sellerService.verifySellerEmail(requestDTO);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller OTP verification successful")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> getSellerProfile(@RequestHeader("Authorization") String token){
        SellerResponseDTO sellerResponseDTO = sellerService.findSellerProfile(token);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller profile fetched successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> createSeller(@Valid @RequestBody SellerCreateDTO sellerCreateDTO){
        SellerResponseDTO sellerResponseDTO = sellerService.createSeller(sellerCreateDTO);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> getSellerById(@PathVariable("id") String id){
        SellerResponseDTO sellerResponseDTO = sellerService.getSellerById(id);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller fetched successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> getSellerByEmail(@PathVariable("email") String email){
        SellerResponseDTO sellerResponseDTO = sellerService.getSellerByEmail(email);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller fetched successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<SellerResponseDTO>>> getAllSellers(
            @RequestParam(value = "status", required = false) AccountStatus accountStatus){
        List<SellerResponseDTO> sellers = sellerService.getAllSellers(accountStatus);
        ApiResponseDTO<List<SellerResponseDTO>> apiResponseDTO = ApiResponseDTO.<List<SellerResponseDTO>>builder()
                .data(sellers)
                .message("Sellers fetched successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> updateSellerProfile(
            @PathVariable("id") UUID sellerId,
            @Valid @RequestBody SellerCreateDTO sellerCreateDTO){
        SellerResponseDTO sellerResponseDTO = sellerService.updateSellerProfile(sellerId, sellerCreateDTO);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller profile updated successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteSeller(@PathVariable("id") UUID sellerId){
        sellerService.deleteSeller(sellerId);
        ApiResponseDTO<Void> apiResponseDTO = ApiResponseDTO.<Void>builder()
                .data(null)
                .message("Seller deleted successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<SellerResponseDTO>> updateSellerAccountStatus(
            @PathVariable("id") UUID sellerId,
            @RequestParam("status") AccountStatus accountStatus){
        SellerResponseDTO sellerResponseDTO = sellerService.updateSellerAccountStatus(sellerId, accountStatus);
        ApiResponseDTO<SellerResponseDTO> apiResponseDTO = ApiResponseDTO.<SellerResponseDTO>builder()
                .data(sellerResponseDTO)
                .message("Seller account status updated successfully")
                .build();
        return ResponseEntity.ok(apiResponseDTO);
    }


}
