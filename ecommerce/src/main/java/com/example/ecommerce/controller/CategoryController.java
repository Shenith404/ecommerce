package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.dto.reponse.CategoryResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationKeyResponseDTO;
import com.example.ecommerce.dto.request.CategoryCreateRequestDTO;
import com.example.ecommerce.dto.request.CategoryUpdateRequestDTO;
import com.example.ecommerce.service.interfaces.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //create
    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> createCategory(
            @RequestPart("name") @NotBlank(message = "Brand name is required") String name,
            @RequestPart(value = "parentCategoryId", required = false) String parentCategoryId,
            @RequestPart(value = "image" ,required = false) MultipartFile image) throws IOException {
        var createdCategory = categoryService.createCategory(
                CategoryCreateRequestDTO.builder()
                        .name(name)
                        .parentCategoryId(parentCategoryId)
                        .build(), image);
        return ResponseEntity.ok(ApiResponseDTO.<CategoryResponseDTO>builder()
                .message("Category created successfully")
                .success(true)
                .data(createdCategory)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    //update
    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> updateCategoryDetails(@Valid @RequestBody CategoryUpdateRequestDTO category) {
        var updatedCategory = categoryService.updateCategoryDetails(category);
        return ResponseEntity.ok(ApiResponseDTO.<CategoryResponseDTO>builder()
                .message("Category updated successfully")
                .success(true)
                .data(updatedCategory)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    //update image
    @PatchMapping(value = "/update/{categoryId}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> updateCategoryImage(
            @PathVariable String categoryId,
            @RequestPart("image") MultipartFile image) throws IOException {
        var updatedCategory = categoryService.updateCategoryImage(categoryId, image);
        return ResponseEntity.ok(ApiResponseDTO.<CategoryResponseDTO>builder()
                .message("Category image updated successfully")
                .success(true)
                .data(updatedCategory)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    //delete
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Category deleted successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    //get by id
    @GetMapping("id/{categoryId}")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> getCategoryById(@PathVariable String categoryId) {
        var category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponseDTO.<CategoryResponseDTO>builder()
                .message("Category retrieved successfully")
                .success(true)
                .data(category)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    //get by slug
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> getCategoryBySlug(@PathVariable String slug) {
        var category = categoryService.getBySlug(slug);
        return ResponseEntity.ok(ApiResponseDTO.<CategoryResponseDTO>builder()
                .message("Category retrieved successfully")
                .success(true)
                .data(category)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }
    //Get all categories
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CategoryResponseDTO>>> getAllCategories(@RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id,asc") String[] sort) {
        var categories = categoryService.getAllCategories(search, page, size, sort);
        return ResponseEntity.ok(ApiResponseDTO.<PageResponseDTO<CategoryResponseDTO>>builder()
                .message("Categories retrieved successfully")
                .success(true)
                .data(categories)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    //get subcategories by parent slug
    @GetMapping("/{parentSlug}/subcategories")
    public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getSubCategoriesByCategory(@PathVariable String parentSlug) {
        var subCategories = categoryService.getSubCategories(parentSlug);
        return ResponseEntity.ok(ApiResponseDTO.<List<CategoryResponseDTO>>builder()
                .message("Subcategories retrieved successfully")
                .success(true)
                .data(subCategories)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // get brands for category
    @GetMapping("/{categoryId}/brands")
    public ResponseEntity<ApiResponseDTO<List<BrandResponseDTO>>> getBrandsForCategory(
            @PathVariable String categoryId) {
        var brands = categoryService.getBrandsForCategory(categoryId);
        return ResponseEntity.ok(ApiResponseDTO.<List<BrandResponseDTO>>builder()
                .message("Brands retrieved successfully")
                .success(true)
                .data(brands)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // add brands to category
    @PostMapping("/{categoryId}/brands")
    public ResponseEntity<ApiResponseDTO<Void>> addBrandsToCategory(
            @PathVariable String categoryId,
            @RequestBody @NotEmpty(message = "Brand IDs must not be empty") Set<String> brandIds) {
        categoryService.addBrandsToCategory(categoryId, brandIds);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Brands added to category successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // remove brands from category
    @DeleteMapping("/{categoryId}/brands")
    public ResponseEntity<ApiResponseDTO<Void>> removeBrandsFromCategory(
            @PathVariable String categoryId,
            @RequestBody @NotEmpty(message = "Brand IDs must not be empty") Set<String> brandIds) {
        categoryService.removeBrandsFromCategory(categoryId, brandIds);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Brands removed from category successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // get specification keys for category
    @GetMapping("/{categoryId}/specification-keys")
    public ResponseEntity<ApiResponseDTO<List<SpecificationKeyResponseDTO>>> getSpecificationKeysForCategory(
            @PathVariable String categoryId) {
        var specKeys = categoryService.getSpecificationKeysForCategory(categoryId);
        return ResponseEntity.ok(ApiResponseDTO.<List<SpecificationKeyResponseDTO>>builder()
                .message("Specification keys retrieved successfully")
                .success(true)
                .data(specKeys)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // add specification keys to category
    @PostMapping("/{categoryId}/specification-keys")
    public ResponseEntity<ApiResponseDTO<Void>> addSpecificationKeysToCategory(
            @PathVariable String categoryId,
            @RequestBody @NotEmpty(message = "Specification key IDs must not be empty") Set<String> specKeyIds) {
        categoryService.addSpecificationKeysToCategory(categoryId, specKeyIds);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Specification keys added to category successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

    // remove specification keys from category
    @DeleteMapping("/{categoryId}/specification-keys")
    public ResponseEntity<ApiResponseDTO<Void>> removeSpecificationKeysFromCategory(
            @PathVariable String categoryId,
            @RequestBody @NotEmpty(message = "Specification key IDs must not be empty") Set<String> specKeyIds) {
        categoryService.removeSpecificationKeysFromCategory(categoryId, specKeyIds);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Specification keys removed from category successfully")
                .success(true)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

}
