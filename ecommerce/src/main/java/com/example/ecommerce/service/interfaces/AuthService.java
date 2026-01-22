package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.AuthResponse;
import com.example.ecommerce.dto.request.LoginRequest;
import com.example.ecommerce.dto.request.SignUpRequestDTO;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import jakarta.mail.MessagingException;

public interface AuthService {
    ApiResponseDTO<AuthResponse>  createUser(SignUpRequestDTO signUpRequestDTO);

    ApiResponseDTO<AuthResponse> loginUser(LoginRequest loginRequest);

    void sendLoginOtp(String email) throws MessagingException;
}
