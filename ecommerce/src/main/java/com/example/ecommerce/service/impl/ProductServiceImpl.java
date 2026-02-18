package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductUpdateRequestDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
    @Transactional
    public ProductResponseDTO update(ProductUpdateRequestDTO requestDTO) {
        Product product = productRepository.findById(UUID.fromString(requestDTO.getId()))
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Product not found with id ")
                );
        // check owner of the product
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!Objects.equals(product.getSeller().getEmail(), sellerEmail)) {
            throw new ResourceNotFoundException("Product not found with id ");
        }
        if(requestDTO.getTitle() != null){
            product.setTitle(requestDTO.getTitle());
        }
        if(requestDTO.getDescription() != null){
            product.setDescription(requestDTO.getDescription());
        }
        if(requestDTO.getCategoryId() != null){
            var category = categoryService.getCategoryEntityById(requestDTO.getCategoryId())
                    .orElseThrow(
                            ()-> new ResourceNotFoundException("Category not found with id ")
                    );
            product.setCategory(category);
        }
        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Product not found with id ")
                );
        // check owner of the product
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!Objects.equals(product.getSeller().getEmail(), sellerEmail)) {
            throw new ResourceNotFoundException("Product not found with id ");
        }
        productRepository.delete(product);
    }

    @Override
    public ProductResponseDTO getById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Product not found with id ")
                );
        return ProductMapper.toDto(product);
    }

    @Override
    public Optional<Product> getEntityById(String id) {
        return productRepository.findById(UUID.fromString(id));
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> getAll(String search, int page, int size, String[] sort) {

        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        String sellerEmail = jwtProvider.getEmailFromHeader();
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<ProductResponseDTO> products;
        if (!Objects.equals(search, "") && search != null) {
            currentPage = productRepository.findSellerProductsBySearchKey(search, sellerEmail, pageable);
        } else {
            currentPage = productRepository.findAllSellerProducts(sellerEmail, pageable);
        }
        products = (currentPage.getContent()).stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products", products.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), products);
    }

    // get products by category
    @Override
    public PageResponseDTO<ProductResponseDTO> getProductsByCategory(String categoryId, String search, int page, int size, String[] sort) {
        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<ProductResponseDTO> products;
        if (!Objects.equals(search, "") && search != null) {
            currentPage = productRepository.findCategoryProductsBySearchKey(UUID.fromString(categoryId), search, pageable);
        } else {
            currentPage = productRepository.findAllCategoryProducts(UUID.fromString(categoryId), pageable);
        }
        products = (currentPage.getContent()).stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products", products.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), products);
    }

    // get products by seller
    @Override
    public PageResponseDTO<ProductResponseDTO> getProductsBySeller(String sellerId, String search, int page, int size, String[] sort) {
        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<ProductResponseDTO> products;
        if (!Objects.equals(search, "") && search != null) {
            currentPage = productRepository.findSellerProductsBySearchKey(search, sellerId, pageable);
        } else {
            currentPage = productRepository.findAllSellerProducts(sellerId, pageable);
        }
        products = (currentPage.getContent()).stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products", products.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), products);
    }

    // get all products (public view)
    @Override
    public PageResponseDTO<ProductResponseDTO> getAllProducts(String search, int page, int size, String[] sort) {
        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<ProductResponseDTO> products;
        if (!Objects.equals(search, "") && search != null) {
            currentPage = productRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = productRepository.findAll(pageable);
        }
        products = (currentPage.getContent()).stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products", products.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), products);
    }


}
