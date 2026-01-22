package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.UserResponseDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByToken(@RequestHeader("Authorization") String token) {
        var user = userService.findUserByJwtToken(token);

        return ResponseEntity.ok(ApiResponseDTO.<UserResponseDTO>builder()
                .data(user)
                .message("User fetched successfully")
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build());

    }

}
