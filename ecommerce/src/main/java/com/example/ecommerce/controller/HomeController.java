package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")

public class HomeController {
    @GetMapping
    public ResponseEntity<ApiResponseDTO<String>> home() {
        return ResponseEntity.ok(
                ApiResponseDTO.<String>builder()
                        .message("Welcome to the E-commerce API")
                        .success(true)
                        .data("E-commerce API is running")
                        .build()
        ) ;
    }
}
