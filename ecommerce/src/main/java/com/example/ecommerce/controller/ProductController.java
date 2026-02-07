package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.service.interfaces.ProductService;
import com.example.ecommerce.service.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    //ownership need

    //create product
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> create(@Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.create(productCreateRequestDTO);
        return ResponseEntity.ok(
                ApiResponseDTO.<ProductResponseDTO>builder()
                        .timestamp(OffsetDateTime.now())
                        .data(productResponseDTO)
                        .success(true)
                        .message("Product created successfully")
                        .build()
        );
    }


}
