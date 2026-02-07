package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.SignUpRequestDTO;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.request.VerificationCodeRequestDTO;
import com.example.ecommerce.service.interfaces.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO<Void>> createUserHandler(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {

        authService.createUser(signUpRequestDTO);

        return ResponseEntity.ok( ApiResponseDTO.<Void>builder()
                .message("User registered successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponseDTO<Void>> createUserHandler(@RequestBody VerificationCodeRequestDTO requestDTO) throws MessagingException {

         authService.sendLoginOtp(requestDTO);

        return ResponseEntity.ok( ApiResponseDTO.<Void>builder()
                .message("User registered successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> createUserHandler(@Valid @RequestBody LoginRequestDTO requestDTO) throws MessagingException {

        var authResponse=  authService.loginUser(requestDTO);

        return ResponseEntity.ok( ApiResponseDTO.<AuthResponseDTO>builder()
                .message("User registered successfully")
                .success(true)
                .data(authResponse.getData())
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }


}
