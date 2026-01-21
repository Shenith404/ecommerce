package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.SignUpRequestDTO;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import jakarta.transaction.Transactional;

public interface AuthService {
    ApiResponseDTO<String>  createUser(SignUpRequestDTO signUpRequestDTO);
}
