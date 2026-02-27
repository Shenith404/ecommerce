package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.ItemCondition;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.ProductResponseDTO;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductUpdateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BrandService brandService;
    private final StoreService storeService;
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
                        () -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId())
                );
        product.setCategory(category);
        //set brand
        if (requestDTO.getBrandId() != null) {
            var brand = brandService.getBrandEntityById(requestDTO.getBrandId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Brand not found with id: " + requestDTO.getBrandId())
                    );
            product.setBrand(brand);
        }
        //set store
        if (requestDTO.getStoreId() != null) {
            var store = storeService.getStoreEntityById(requestDTO.getStoreId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Store not found with id: " + requestDTO.getStoreId())
                    );
            product.setStore(store);
        }

        //set seller — get email from token
        String sellerEmail = jwtProvider.getEmailFromHeader();
        var seller = sellerService.getSellerEntityByEmail(sellerEmail)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seller not found with email: " + sellerEmail)
                );
        product.setSeller(seller);
        //set slug
        product.setSlug(generateUniqueSlug(requestDTO.getTitle(), null));
        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    public ProductResponseDTO update(ProductUpdateRequestDTO requestDTO) {
        Product product = productRepository.findById(UUID.fromString(requestDTO.getId()))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id: " + requestDTO.getId())
                );
        // check owner of the product
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!Objects.equals(product.getSeller().getEmail(), sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this product");
        }
        if(requestDTO.getTitle() != null){
            product.setTitle(requestDTO.getTitle());
            product.setSlug(generateUniqueSlug(requestDTO.getTitle(), product.getSlug()));
        }
        if(requestDTO.getDescription() != null){
            product.setDescription(requestDTO.getDescription());
        }
        if(requestDTO.getItemCondition() != null){
            product.setItemCondition(ItemCondition.valueOf(requestDTO.getItemCondition()));
        }
        if(requestDTO.getSpecifications() != null){
            product.setSpecifications(requestDTO.getSpecifications());
        }

        if (requestDTO.getCategoryId() != null) {
            var category = categoryService.getCategoryEntityById(requestDTO.getCategoryId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId())
                    );
            product.setCategory(category);
        }
        if (requestDTO.getBrandId() != null) {
            var brand = brandService.getBrandEntityById(requestDTO.getBrandId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Brand not found with id: " + requestDTO.getBrandId())
                    );
            product.setBrand(brand);
        }
        if (requestDTO.getStoreId() != null) {
            var store = storeService.getStoreEntityById(requestDTO.getStoreId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Store not found with id: " + requestDTO.getStoreId())
                    );
            product.setStore(store);
        }

        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    public void delete(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id: " + id)
                );
        // check owner of the product
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!Objects.equals(product.getSeller().getEmail(), sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to delete this product");
        }
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id: " + id)
                );
        return ProductMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with slug: " + slug)
                );
        return ProductMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getEntityById(String id) {
        return productRepository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
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
        if (search != null && !search.isBlank()) {
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
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getProductsByCategory(String categorySlug, String search, int page, int size, String[] sort) {
        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<ProductResponseDTO> products;
        if (search != null && !search.isBlank()) {
            currentPage = productRepository.findCategoryProductsBySearchKey(categorySlug, search, pageable);
        } else {
            currentPage = productRepository.findAllCategoryProducts(categorySlug, pageable);
        }
        products = (currentPage.getContent()).stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products", products.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), products);
    }

    // get products by seller (public view)
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getProductsBySeller(String sellerId, String search, int page, int size, String[] sort) {
        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        UUID sellerUUID = UUID.fromString(sellerId);

        Page<Product> currentPage;
        List<ProductResponseDTO> products;
        if (search != null && !search.isBlank()) {
            currentPage = productRepository.findProductsBySellerIdAndSearchKey(sellerUUID, search, pageable);
        } else {
            currentPage = productRepository.findAllProductsBySellerId(sellerUUID, pageable);
        }
        products = currentPage.getContent().stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products for seller {}", products.size(), sellerId);

        return new PageResponseDTO<>(pageable.getPageNumber(), currentPage.getTotalPages(), products);
    }

    // get all products (public view)
    @Override
    @Transactional(readOnly = true)
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
        if (search != null && !search.isBlank()) {
            currentPage = productRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = productRepository.findAll(pageable);
        }
        products = (currentPage.getContent()).stream().map(ProductMapper::toDto).toList();

        LOGGER.info("Retrieved {} products", products.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), products);
    }


    private String generateUniqueSlug(String title, String currentSlug) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // keep only letters, digits, spaces, hyphens
                .trim()
                .replaceAll("[\\s-]+", "-");        // collapse spaces/hyphens into one dash

        String candidate = baseSlug;
        int counter = 1;

        while (productRepository.existsBySlug(candidate)
                && !candidate.equals(currentSlug)) {
            candidate = baseSlug + "-" + counter++;
            if(counter > 100) { // safety check to prevent infinite loop
                throw new IllegalArgumentException("Unable to generate unique slug for product title: " + title);
            }
        }
        return candidate;
    }

}
