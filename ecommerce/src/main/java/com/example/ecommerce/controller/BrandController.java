package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.BrandCreateRequestDTO;
import com.example.ecommerce.service.interfaces.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
@Validated
public class BrandController {

    private final BrandService brandService;

    // Create brand (admin only)
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<BrandResponseDTO>> createBrand(
            @RequestPart("name") @NotBlank(message = "Brand name is required") String name,
            @RequestPart(value = "image" ,required = false) MultipartFile image) throws IOException {
        BrandCreateRequestDTO dto = new BrandCreateRequestDTO();
        dto.setName(name);
        var created = brandService.createBrand(dto, image);
        return ResponseEntity.ok(
                ApiResponseDTO.<BrandResponseDTO>builder()
                        .message("Brand created successfully")
                        .success(true)
                        .data(created)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Update brand details (admin only)
    @PatchMapping("/update/{brandId}")
    public ResponseEntity<ApiResponseDTO<BrandResponseDTO>> updateBrandDetails(
            @PathVariable String brandId,
            @Valid @RequestBody BrandCreateRequestDTO dto) {
        var updated = brandService.updateBrandDetails(brandId, dto);
        return ResponseEntity.ok(
                ApiResponseDTO.<BrandResponseDTO>builder()
                        .message("Brand updated successfully")
                        .success(true)
                        .data(updated)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Update brand logo (admin only)
    @PatchMapping(value = "/update/{brandId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<BrandResponseDTO>> updateBrandLogo(
            @PathVariable String brandId,
            @RequestPart("image") MultipartFile image) throws IOException {
        var updated = brandService.updateBrandLogo(brandId, image);
        return ResponseEntity.ok(
                ApiResponseDTO.<BrandResponseDTO>builder()
                        .message("Brand logo updated successfully")
                        .success(true)
                        .data(updated)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Delete brand (admin only)
    @DeleteMapping("/delete/{brandId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteBrand(@PathVariable String brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .message("Brand deleted successfully")
                        .success(true)
                        .data(null)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Get brand by slug (public)
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponseDTO<BrandResponseDTO>> getBrandBySlug(@PathVariable String slug) {
        var brand = brandService.getBrandBySlug(slug);
        return ResponseEntity.ok(
                ApiResponseDTO.<BrandResponseDTO>builder()
                        .message("Brand retrieved successfully")
                        .success(true)
                        .data(brand)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Get all brands (public)
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<BrandResponseDTO>>> getAllBrands(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        var brands = brandService.getAllBrands(search, page, size, sort);
        return ResponseEntity.ok(
                ApiResponseDTO.<PageResponseDTO<BrandResponseDTO>>builder()
                        .message("Brands retrieved successfully")
                        .success(true)
                        .data(brands)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }
}

