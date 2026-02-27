package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.StoreResponseDTO;
import com.example.ecommerce.dto.request.StoreCreateRequestDTO;
import com.example.ecommerce.service.interfaces.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    // create
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDTO<StoreResponseDTO>> createStore(
            @RequestPart("storeName") String storeName,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "returnPolicy", required = false) String returnPolicy,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "banner", required = false) MultipartFile banner) throws IOException {
        if (storeName == null || storeName.isBlank()) {
            throw new IllegalArgumentException("Store name is required");
        }
        if (storeName.length() < 3 || storeName.length() > 100) {
            throw new IllegalArgumentException("Store name must be between 3 and 100 characters");
        }
        var createdStore = storeService.create(
                StoreCreateRequestDTO.builder()
                        .storeName(storeName)
                        .description(description)
                        .returnPolicy(returnPolicy)
                        .build(),
                logo,
                banner);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.<StoreResponseDTO>builder()
                .message("Store created successfully")
                .success(true)
                .data(createdStore)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // update details
    @PatchMapping("/update/{storeId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDTO<StoreResponseDTO>> updateStoreDetails(
            @PathVariable String storeId,
            @Valid @RequestBody StoreCreateRequestDTO dto) {
        var updatedStore = storeService.updateDetails(storeId, dto);
        return ResponseEntity.ok(ApiResponseDTO.<StoreResponseDTO>builder()
                .message("Store updated successfully")
                .success(true)
                .data(updatedStore)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // update logo
    @PatchMapping(value = "/update/{storeId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDTO<StoreResponseDTO>> updateStoreLogo(
            @PathVariable String storeId,
            @RequestPart("logo") MultipartFile logo) throws IOException {
        var updatedStore = storeService.updateLogo(storeId, logo);
        return ResponseEntity.ok(ApiResponseDTO.<StoreResponseDTO>builder()
                .message("Store logo updated successfully")
                .success(true)
                .data(updatedStore)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // update banner
    @PatchMapping(value = "/update/{storeId}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDTO<StoreResponseDTO>> updateStoreBanner(
            @PathVariable String storeId,
            @RequestPart("banner") MultipartFile banner) throws IOException {
        var updatedStore = storeService.updateBanner(storeId, banner);
        return ResponseEntity.ok(ApiResponseDTO.<StoreResponseDTO>builder()
                .message("Store banner updated successfully")
                .success(true)
                .data(updatedStore)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // delete
    @DeleteMapping("/delete/{storeId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteStore(@PathVariable String storeId) {
        storeService.delete(storeId);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Store deleted successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // get by id
    @GetMapping("/id/{storeId}")
    public ResponseEntity<ApiResponseDTO<StoreResponseDTO>> getStoreById(@PathVariable String storeId) {
        var store = storeService.getById(storeId);
        return ResponseEntity.ok(ApiResponseDTO.<StoreResponseDTO>builder()
                .message("Store retrieved successfully")
                .success(true)
                .data(store)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // get by seo slug
    @GetMapping("/slug/{seoSlug}")
    public ResponseEntity<ApiResponseDTO<StoreResponseDTO>> getStoreBySeoSlug(@PathVariable String seoSlug) {
        var store = storeService.findBySeoSlug(seoSlug);
        return ResponseEntity.ok(ApiResponseDTO.<StoreResponseDTO>builder()
                .message("Store retrieved successfully")
                .success(true)
                .data(store)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // get all stores
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<StoreResponseDTO>>> getAllStores(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        var stores = storeService.getAll(search, page, size, sort);
        return ResponseEntity.ok(ApiResponseDTO.<PageResponseDTO<StoreResponseDTO>>builder()
                .message("Stores retrieved successfully")
                .success(true)
                .data(stores)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }
}

