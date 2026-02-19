package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductUpdateRequestDTO;
import com.example.ecommerce.model.Product;

import java.util.Optional;

public interface ProductService {
    public ProductResponseDTO create(ProductCreateRequestDTO requestDTO);
    public ProductResponseDTO update(ProductUpdateRequestDTO requestDTO);
    public void delete(String id);
    public ProductResponseDTO getById(String id);
    public Optional<Product> getEntityById(String id);
    public PageResponseDTO<ProductResponseDTO> getAll(String search, int page, int size, String[] sort);
    public PageResponseDTO<ProductResponseDTO> getProductsByCategory(String categoryId, String search, int page, int size, String[] sort);
    public PageResponseDTO<ProductResponseDTO> getProductsBySeller( String search, int page, int size, String[] sort);
    public PageResponseDTO<ProductResponseDTO> getAllProducts(String search, int page, int size, String[] sort);
}
