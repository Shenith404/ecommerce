package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.interfaces.CategoryService;
import com.example.ecommerce.service.interfaces.ProductService;
import com.example.ecommerce.service.interfaces.SellerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SellerService sellerService;
    private final JwtProvider jwtProvider;

    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    public ProductResponseDTO create(ProductCreateRequestDTO requestDTO) {
        Product product = ProductMapper.toEntity(requestDTO);
        //set category
        var category = categoryService.getCategoryEntityById(requestDTO.getCategoryId())
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Category not found with id ")
                );
        product.setCategory(category);
        //set seller
        //get email from token
        String sellerEmail = jwtProvider.getEmailFromHeader();
        var seller = sellerService.getSellerEntityByEmail(sellerEmail)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Seller not found with Email ")
                );
        product.setSeller(seller);

        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ProductResponseDTO update(ProductCreateRequestDTO requestDTO) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(String id) {

    }

    @Override
    public ProductResponseDTO getById(String id) {
        return null;
    }

    @Override
    public Optional<Product> getEntityById(String id) {
        return Optional.empty();
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> getAll(String search, int page, int size, String[] sort) {
        return null;
    }
}
