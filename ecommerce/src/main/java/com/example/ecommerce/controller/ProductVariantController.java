package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.ProductVariantResponseDTO;
import com.example.ecommerce.dto.request.ProductVariantCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductVariantUpdateRequestDTO;
import com.example.ecommerce.service.interfaces.ProductVariantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService productVariantService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<ProductVariantResponseDTO>> create(
            @RequestParam("variantData") String variantDataJson,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        ProductVariantCreateRequestDTO dto = objectMapper.readValue(variantDataJson, ProductVariantCreateRequestDTO.class);

        Set<ConstraintViolation<ProductVariantCreateRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errorMsg);
        }

        var createdVariant = productVariantService.createVariant(dto, image);
        return ResponseEntity.ok(ApiResponseDTO.<ProductVariantResponseDTO>builder()
                .message("Product Variant Created Successfully")
                .data(createdVariant)
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductVariantResponseDTO>> update(
            @PathVariable String id,
            @RequestBody @Valid ProductVariantUpdateRequestDTO dto
    ) {
        var updatedVariant = productVariantService.update(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.<ProductVariantResponseDTO>builder()
                .message("Product Variant Updated Successfully")
                .data(updatedVariant)
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<ProductVariantResponseDTO>> updateImage(
            @PathVariable String id,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        var updatedVariant = productVariantService.updateImage(id, image);
        return ResponseEntity.ok(ApiResponseDTO.<ProductVariantResponseDTO>builder()
                .message("Product Variant Image Updated Successfully")
                .data(updatedVariant)
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable String id) {
        productVariantService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Product Variant Deleted Successfully")
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductVariantResponseDTO>> getById(@PathVariable String id) {
        var variant = productVariantService.getById(id);
        return ResponseEntity.ok(ApiResponseDTO.<ProductVariantResponseDTO>builder()
                .message("Product Variant Retrieved Successfully")
                .data(variant)
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponseDTO<List<ProductVariantResponseDTO>>> getByProduct(@PathVariable String productId) {
        var variants = productVariantService.getAll(productId);
        return ResponseEntity.ok(ApiResponseDTO.<List<ProductVariantResponseDTO>>builder()
                .message("Product Variants Retrieved Successfully")
                .data(variants)
                .success(true)
                .timestamp(OffsetDateTime.now())
                .build());
    }

}
