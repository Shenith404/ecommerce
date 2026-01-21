package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.UserRole;
import com.example.ecommerce.dto.SignUpRequestDTO;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.interfaces.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public ApiResponseDTO<String> createUser(SignUpRequestDTO signUpRequestDTO) {
        var user  = userRepository.findByEmail(signUpRequestDTO.getEmail());
        if(user.isPresent()){
            throw new ResourceNotFoundException("User already exists with email: " + signUpRequestDTO.getEmail());
        }else{
            User newUser = new User();
            newUser.setFullName(signUpRequestDTO.getFullName());
            newUser.setEmail(signUpRequestDTO.getEmail());
            newUser.setRole(UserRole.ROLE_CUSTOMER);
            newUser.setPassword(passwordEncoder.encode(signUpRequestDTO.getOtp()));
            newUser  = userRepository.save(newUser);
            LOGGER.info("New user created with email: {}", signUpRequestDTO.getEmail());

        }
        return ApiResponseDTO.<String>builder()
                .data("User created successfully")
                .message("User created successfully")
                .success(true)
                .build();



        }
}
