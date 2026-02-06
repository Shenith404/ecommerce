package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.CategoryResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.CategoryCreateRequestDTO;
import com.example.ecommerce.dto.request.CategoryUpdateRequestDTO;
import com.example.ecommerce.service.interfaces.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //create
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryCreateRequestDTO category){
        var createdCategory = categoryService.createCategory(category);
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
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> updateCategory(@Valid @RequestBody CategoryUpdateRequestDTO category) {
        var updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(ApiResponseDTO.<CategoryResponseDTO>builder()
                .message("Category updated successfully")
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

    //get
    @GetMapping("/{categoryId}")
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

    //get subcategories by category id
    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getSubCategoriesByCategory(@PathVariable String categoryId) {
        var subCategories = categoryService.getSubCategories(categoryId);
        return ResponseEntity.ok(ApiResponseDTO.<List<CategoryResponseDTO>>builder()
                .message("Subcategories retrieved successfully")
                .success(true)
                .data(subCategories)
                .timestamp(OffsetDateTime.now())
                .build()
        );
    }

}
