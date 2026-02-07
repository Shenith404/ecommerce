package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.SignUpRequestDTO;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.request.VerificationCodeRequestDTO;
import jakarta.mail.MessagingException;

public interface AuthService {
    ApiResponseDTO<Void>  createUser(SignUpRequestDTO signUpRequestDTO);

    ApiResponseDTO<AuthResponseDTO> loginUser(LoginRequestDTO loginRequestDTO);

    void sendLoginOtp(VerificationCodeRequestDTO requestDTO) throws MessagingException;
}
