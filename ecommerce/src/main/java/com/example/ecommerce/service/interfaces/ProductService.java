package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.FilteredProductResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductFilterRequestDTO;
import com.example.ecommerce.dto.request.ProductUpdateRequestDTO;
import com.example.ecommerce.model.Product;

import java.util.Optional;

public interface ProductService {
    ProductResponseDTO create(ProductCreateRequestDTO requestDTO);
    ProductResponseDTO update(ProductUpdateRequestDTO requestDTO);
    void delete(String id);
    ProductResponseDTO getById(String id);
    Optional<Product> getEntityById(String id);
    PageResponseDTO<ProductResponseDTO> getAll(String search, int page, int size, String[] sort);
    PageResponseDTO<ProductResponseDTO> getProductsByCategory(String categorySlug, String search, int page, int size, String[] sort);
    PageResponseDTO<ProductResponseDTO> getProductsBySeller(String sellerId, String search, int page, int size, String[] sort);
    PageResponseDTO<ProductResponseDTO> getAllProducts(String search, int page, int size, String[] sort);
    ProductResponseDTO getBySlug(String slug);

    /**
     * Advanced filtering with live sidebar facets.
     * Every call returns filtered products + updated sidebar data (brands, price range,
     * conditions, ratings, specifications) scoped to the current filter context.
     */
    FilteredProductResponseDTO filterProducts(ProductFilterRequestDTO filter);
}


