package com.example.ecommerce.controller;

import com.example.ecommerce.dto.SignUpRequestDTO;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO<User>> createUserHandler(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        User user = new User();
        user.setEmail(signUpRequestDTO.getEmail());
        user.setFullName(signUpRequestDTO.getFullName());

        return ResponseEntity.ok( ApiResponseDTO.<User>builder()
                .message("User registered successfully")
                .success(true)
                .data(user)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }
}
