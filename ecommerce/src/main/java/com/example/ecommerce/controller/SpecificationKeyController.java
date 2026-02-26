package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationKeyResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationOptionResponseDTO;
import com.example.ecommerce.dto.request.SpecificationKeyCreateRequestDTO;
import com.example.ecommerce.dto.request.SpecificationKeyUpdateRequestDTO;
import com.example.ecommerce.dto.request.SpecificationOptionCreateRequestDTO;
import com.example.ecommerce.service.interfaces.SpecificationKeyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/specification-keys")
@RequiredArgsConstructor
@Validated
public class SpecificationKeyController {
    
    private final SpecificationKeyService specificationKeyService;

    // Create specification key
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<SpecificationKeyResponseDTO>> createSpecificationKey(
            @Valid @RequestBody SpecificationKeyCreateRequestDTO dto) {
        var created = specificationKeyService.createSpecificationKey(dto);
        return ResponseEntity.ok(
                ApiResponseDTO.<SpecificationKeyResponseDTO>builder()
                        .message("Specification key created successfully")
                        .success(true)
                        .data(created)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Update specification key
    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDTO<SpecificationKeyResponseDTO>> updateSpecificationKey(
            @Valid @RequestBody SpecificationKeyUpdateRequestDTO dto) {
        var updated = specificationKeyService.updateSpecificationKey(dto);
        return ResponseEntity.ok(
                ApiResponseDTO.<SpecificationKeyResponseDTO>builder()
                        .message("Specification key updated successfully")
                        .success(true)
                        .data(updated)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Delete specification key
    @DeleteMapping("/delete/{specKeyId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteSpecificationKey(@PathVariable String specKeyId) {
        specificationKeyService.deleteSpecificationKey(specKeyId);
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .message("Specification key deleted successfully")
                        .success(true)
                        .data(null)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Get specification key by ID
    @GetMapping("/{specKeyId}")
    public ResponseEntity<ApiResponseDTO<SpecificationKeyResponseDTO>> getSpecificationKeyById(
            @PathVariable String specKeyId) {
        var specKey = specificationKeyService.getSpecificationKeyById(specKeyId);
        return ResponseEntity.ok(
                ApiResponseDTO.<SpecificationKeyResponseDTO>builder()
                        .message("Specification key retrieved successfully")
                        .success(true)
                        .data(specKey)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Get all specification keys with pagination and search
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<SpecificationKeyResponseDTO>>> getAllSpecificationKeys(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {
        var specKeys = specificationKeyService.getAllSpecificationKeys(search, page, size, sort);
        return ResponseEntity.ok(
                ApiResponseDTO.<PageResponseDTO<SpecificationKeyResponseDTO>>builder()
                        .message("Specification keys retrieved successfully")
                        .success(true)
                        .data(specKeys)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Add options to specification key
    @PostMapping("/{specKeyId}/options")
    public ResponseEntity<ApiResponseDTO<List<SpecificationOptionResponseDTO>>> addOptionsToSpecificationKey(
            @PathVariable String specKeyId,
            @Valid @RequestBody Set<SpecificationOptionCreateRequestDTO> options) {
        var addedOptions = specificationKeyService.addOptionsToSpecificationKey(specKeyId, options);
        return ResponseEntity.ok(
                ApiResponseDTO.<List<SpecificationOptionResponseDTO>>builder()
                        .message("Options added successfully")
                        .success(true)
                        .data(addedOptions)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Remove options from specification key
    @DeleteMapping("/{specKeyId}/options")
    public ResponseEntity<ApiResponseDTO<Void>> removeOptionsFromSpecificationKey(
            @PathVariable String specKeyId,
            @RequestBody @NotEmpty(message = "Option IDs must not be empty") Set<@NotBlank String> optionIds) {
        specificationKeyService.removeOptionsFromSpecificationKey(specKeyId, optionIds);
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .message("Options removed successfully")
                        .success(true)
                        .data(null)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }

    // Get options for specification key
    @GetMapping("/{specKeyId}/options")
    public ResponseEntity<ApiResponseDTO<List<SpecificationOptionResponseDTO>>> getOptionsForSpecificationKey(
            @PathVariable String specKeyId) {
        var options = specificationKeyService.getOptionsForSpecificationKey(specKeyId);
        return ResponseEntity.ok(
                ApiResponseDTO.<List<SpecificationOptionResponseDTO>>builder()
                        .message("Options retrieved successfully")
                        .success(true)
                        .data(options)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }
}
