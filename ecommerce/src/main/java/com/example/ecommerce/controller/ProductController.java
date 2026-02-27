package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.FilteredProductResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductFilterRequestDTO;
import com.example.ecommerce.dto.request.ProductUpdateRequestDTO;
import com.example.ecommerce.service.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> createProduct(
            @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO) {
        var createdProduct = productService.create(productCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.<ProductResponseDTO>builder()
                        .message("Product created successfully")
                        .success(true)
                        .data(createdProduct)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> updateProduct(
            @Valid @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO) {
        var updatedProduct = productService.update(productUpdateRequestDTO);
        return ResponseEntity.ok(
                ApiResponseDTO.<ProductResponseDTO>builder()
                        .message("Product updated successfully")
                        .success(true)
                        .data(updatedProduct)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProduct(@PathVariable String productId) {
        productService.delete(productId);
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .message("Product deleted successfully")
                        .success(true)
                        .data(null)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductById(@PathVariable String productId) {
        var product = productService.getById(productId);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic())
                .body(ApiResponseDTO.<ProductResponseDTO>builder()
                        .message("Product retrieved successfully")
                        .success(true)
                        .data(product)
                        .timestamp(OffsetDateTime.now())
                        .build()
                );
    }


    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductBySlug(@PathVariable String slug) {
        var product = productService.getBySlug(slug);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic())
                .body(ApiResponseDTO.<ProductResponseDTO>builder()
                        .message("Product retrieved successfully")
                        .success(true)
                        .data(product)
                        .timestamp(OffsetDateTime.now())
                        .build()
                );
    }


    @GetMapping("/seller/my-products")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getMyProducts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        var products = productService.getAll(search, page, size, sort);
        return ResponseEntity.ok(
                ApiResponseDTO.<PageResponseDTO<ProductResponseDTO>>builder()
                        .message("Products retrieved successfully")
                        .success(true)
                        .data(products)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        var products = productService.getAllProducts(search, page, size, sort);
        return ResponseEntity.ok(
                ApiResponseDTO.<PageResponseDTO<ProductResponseDTO>>builder()
                        .message("Products retrieved successfully")
                        .success(true)
                        .data(products)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @GetMapping("/category/{categorySlug}")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getProductsByCategory(
            @PathVariable String categorySlug,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        var products = productService.getProductsByCategory(categorySlug, search, page, size, sort);
        return ResponseEntity.ok(
                ApiResponseDTO.<PageResponseDTO<ProductResponseDTO>>builder()
                        .message("Products retrieved successfully")
                        .success(true)
                        .data(products)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @GetMapping("/seller/{sellerId}/products")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getProductsBySeller(
            @PathVariable String sellerId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        var products = productService.getProductsBySeller(sellerId, search, page, size, sort);
        return ResponseEntity.ok(
                ApiResponseDTO.<PageResponseDTO<ProductResponseDTO>>builder()
                        .message("Products retrieved successfully")
                        .success(true)
                        .data(products)
                        .timestamp(OffsetDateTime.now())
                        .build()
        );
    }


    @PostMapping("/filter")
    public ResponseEntity<ApiResponseDTO<FilteredProductResponseDTO>> filterProducts(
            @Valid @RequestBody ProductFilterRequestDTO filterRequest) {
        var result = productService.filterProducts(filterRequest);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES).cachePublic())
                .body(ApiResponseDTO.<FilteredProductResponseDTO>builder()
                        .message("Products filtered successfully")
                        .success(true)
                        .data(result)
                        .timestamp(OffsetDateTime.now())
                        .build()
                );
    }
}

