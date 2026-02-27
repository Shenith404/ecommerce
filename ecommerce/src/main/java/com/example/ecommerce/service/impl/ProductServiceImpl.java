package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.CacheConfig;
import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.ItemCondition;
import com.example.ecommerce.dto.reponse.*;
import com.example.ecommerce.dto.request.ProductCreateRequestDTO;
import com.example.ecommerce.dto.request.ProductFilterRequestDTO;
import com.example.ecommerce.dto.request.ProductUpdateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.interfaces.*;
import com.example.ecommerce.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    /** Whitelisted sort fields — prevents arbitrary column injection */
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "createdAt", "title", "averageRating"
    );

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final StoreService storeService;
    private final SellerService sellerService;
    private final JwtProvider jwtProvider;

    // -----------------------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------------------
    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    @CacheEvict(cacheNames = CacheConfig.PRODUCTS_FILTER_CACHE, allEntries = true)
    public ProductResponseDTO create(ProductCreateRequestDTO requestDTO) {
        Product product = ProductMapper.toEntity(requestDTO);

        var category = categoryService.getCategoryEntityById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId()));
        product.setCategory(category);

        if (requestDTO.getBrandId() != null) {
            var brand = brandService.getBrandEntityById(requestDTO.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + requestDTO.getBrandId()));
            product.setBrand(brand);
        }

        if (requestDTO.getStoreId() != null) {
            var store = storeService.getStoreEntityById(requestDTO.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + requestDTO.getStoreId()));
            product.setStore(store);
        }

        String sellerEmail = jwtProvider.getEmailFromHeader();
        var seller = sellerService.getSellerEntityByEmail(sellerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email: " + sellerEmail));
        product.setSeller(seller);
        product.setSlug(generateUniqueSlug(requestDTO.getTitle(), null));

        return ProductMapper.toDto(productRepository.save(product));
    }

    // -----------------------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------------------
    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConfig.PRODUCTS_FILTER_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConfig.PRODUCT_BY_SLUG_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConfig.PRODUCT_BY_ID_CACHE, allEntries = true)
    })
    public ProductResponseDTO update(ProductUpdateRequestDTO requestDTO) {
        Product product = productRepository.findById(UUID.fromString(requestDTO.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + requestDTO.getId()));

        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!Objects.equals(product.getSeller().getEmail(), sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this product");
        }

        if (requestDTO.getTitle() != null) {
            product.setTitle(requestDTO.getTitle());
            product.setSlug(generateUniqueSlug(requestDTO.getTitle(), product.getSlug()));
        }
        if (requestDTO.getDescription() != null) product.setDescription(requestDTO.getDescription());
        if (requestDTO.getItemCondition() != null) product.setItemCondition(ItemCondition.valueOf(requestDTO.getItemCondition()));
        if (requestDTO.getSpecifications() != null) product.setSpecifications(requestDTO.getSpecifications());

        if (requestDTO.getCategoryId() != null) {
            var category = categoryService.getCategoryEntityById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId()));
            product.setCategory(category);
        }
        if (requestDTO.getBrandId() != null) {
            var brand = brandService.getBrandEntityById(requestDTO.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + requestDTO.getBrandId()));
            product.setBrand(brand);
        }
        if (requestDTO.getStoreId() != null) {
            var store = storeService.getStoreEntityById(requestDTO.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + requestDTO.getStoreId()));
            product.setStore(store);
        }

        return ProductMapper.toDto(productRepository.save(product));
    }

    // -----------------------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------------------
    @Override
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConfig.PRODUCTS_FILTER_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConfig.PRODUCT_BY_SLUG_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConfig.PRODUCT_BY_ID_CACHE, allEntries = true)
    })
    public void delete(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!Objects.equals(product.getSeller().getEmail(), sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to delete this product");
        }
        productRepository.delete(product);
    }

    // -----------------------------------------------------------------------
    // GET BY ID
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheConfig.PRODUCT_BY_ID_CACHE, key = "#id")
    public ProductResponseDTO getById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductMapper.toDto(product);
    }

    // -----------------------------------------------------------------------
    // GET BY SLUG
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheConfig.PRODUCT_BY_SLUG_CACHE, key = "#slug")
    public ProductResponseDTO getBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        return ProductMapper.toDto(product);
    }

    // -----------------------------------------------------------------------
    // GET ENTITY BY ID
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getEntityById(String id) {
        return productRepository.findById(UUID.fromString(id));
    }

    // -----------------------------------------------------------------------
    // GET ALL — seller's own products (authenticated)
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getAll(String search, int page, int size, String[] sort) {
        sort = validateSortArray(sort);
        String sellerEmail = jwtProvider.getEmailFromHeader();
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage = (search != null && !search.isBlank())
                ? productRepository.findSellerProductsBySearchKey(search, sellerEmail, pageable)
                : productRepository.findAllSellerProducts(sellerEmail, pageable);

        List<ProductResponseDTO> products = currentPage.getContent().stream().map(ProductMapper::toDto).toList();
        LOGGER.info("Retrieved {} seller products", products.size());
        return new PageResponseDTO<>(pageable.getPageNumber(), currentPage.getTotalPages(), products);
    }

    // -----------------------------------------------------------------------
    // GET BY CATEGORY
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getProductsByCategory(String categorySlug, String search, int page, int size, String[] sort) {
        sort = validateSortArray(sort);
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage = (search != null && !search.isBlank())
                ? productRepository.findCategoryProductsBySearchKey(categorySlug, search, pageable)
                : productRepository.findAllCategoryProducts(categorySlug, pageable);

        List<ProductResponseDTO> products = currentPage.getContent().stream().map(ProductMapper::toDto).toList();
        LOGGER.info("Retrieved {} products for category {}", products.size(), categorySlug);
        return new PageResponseDTO<>(pageable.getPageNumber(), currentPage.getTotalPages(), products);
    }

    // -----------------------------------------------------------------------
    // GET BY SELLER (public view)
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getProductsBySeller(String sellerId, String search, int page, int size, String[] sort) {
        sort = validateSortArray(sort);
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        UUID sellerUUID = UUID.fromString(sellerId);

        Page<Product> currentPage = (search != null && !search.isBlank())
                ? productRepository.findProductsBySellerIdAndSearchKey(sellerUUID, search, pageable)
                : productRepository.findAllProductsBySellerId(sellerUUID, pageable);

        List<ProductResponseDTO> products = currentPage.getContent().stream().map(ProductMapper::toDto).toList();
        LOGGER.info("Retrieved {} products for seller {}", products.size(), sellerId);
        return new PageResponseDTO<>(pageable.getPageNumber(), currentPage.getTotalPages(), products);
    }

    // -----------------------------------------------------------------------
    // GET ALL PRODUCTS (public)
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductResponseDTO> getAllProducts(String search, int page, int size, String[] sort) {
        sort = validateSortArray(sort);
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> currentPage = (search != null && !search.isBlank())
                ? productRepository.findBySearchKey(search, pageable)
                : productRepository.findAll(pageable);

        List<ProductResponseDTO> products = currentPage.getContent().stream().map(ProductMapper::toDto).toList();
        LOGGER.info("Retrieved {} products", products.size());
        return new PageResponseDTO<>(pageable.getPageNumber(), currentPage.getTotalPages(), products);
    }

    // -----------------------------------------------------------------------
    // ADVANCED FILTER — with live sidebar facets
    // -----------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public FilteredProductResponseDTO filterProducts(ProductFilterRequestDTO filter) {

        // Cap page size to protect DB from oversized scans
        int safeSize = Math.min(filter.getSize(), 50);

        Sort sort = buildSort(filter.getSortBy(), filter.getSortDir());
        Pageable pageable = PageRequest.of(filter.getPage(), safeSize, sort);

        // Build and execute the dynamic Specification query
        Specification<Product> spec = ProductSpecification.withFilters(filter);
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        List<Product> pageProducts = productPage.getContent();
        List<ProductResponseDTO> productDTOs = pageProducts.stream()
                .map(ProductMapper::toDto)
                .toList();

        // Collect ALL matched IDs (across all pages) for accurate facet counts
        // This second query only fetches IDs — lightweight
        List<UUID> allMatchedIds = productRepository.findAll(spec)
                .stream()
                .map(Product::getId)
                .toList();

        LOGGER.info("Filter matched {} total products — page {}/{}",
                productPage.getTotalElements(), filter.getPage(), productPage.getTotalPages());

        // Build sidebar facets from full matched set
        List<BrandFacetDTO> brandFacets = new ArrayList<>();
        PriceRangeDTO priceRange = null;
        List<String> availableConditions = new ArrayList<>();
        Map<Integer, Long> ratingCounts = new LinkedHashMap<>();
        Map<String, List<String>> availableSpecifications = new LinkedHashMap<>();

        if (!allMatchedIds.isEmpty()) {
            brandFacets             = buildBrandFacets(allMatchedIds);
            priceRange              = buildPriceRange(allMatchedIds);
            availableConditions     = buildConditionFacets(allMatchedIds);
            ratingCounts            = buildRatingFacets(allMatchedIds);
            // Spec facets use current page products (variant specs loaded lazily)
            availableSpecifications = buildSpecificationFacets(pageProducts);
        }

        return FilteredProductResponseDTO.builder()
                .products(productDTOs)
                .page(productPage.getNumber())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .availableBrands(brandFacets)
                .priceRange(priceRange)
                .availableConditions(availableConditions)
                .ratingCounts(ratingCounts)
                .availableSpecifications(availableSpecifications)
                .build();
    }

    // -----------------------------------------------------------------------
    // PRIVATE FACET BUILDERS
    // -----------------------------------------------------------------------

    private List<BrandFacetDTO> buildBrandFacets(List<UUID> productIds) {
        return productRepository.findBrandFacetsByProductIds(productIds)
                .stream()
                .map(row -> BrandFacetDTO.builder()
                        .id(row[0].toString())
                        .name((String) row[1])
                        .slug((String) row[2])
                        .count(((Number) row[3]).longValue())
                        .build())
                .toList();
    }

    private PriceRangeDTO buildPriceRange(List<UUID> productIds) {
        Object[] result = productRepository.findPriceRangeByProductIds(productIds);
        if (result == null || result[0] == null) return null;
        return PriceRangeDTO.builder()
                .min((BigDecimal) result[0])
                .max((BigDecimal) result[1])
                .build();
    }

    private List<String> buildConditionFacets(List<UUID> productIds) {
        return productRepository.findConditionFacetsByProductIds(productIds)
                .stream()
                .map(row -> row[0].toString())
                .toList();
    }

    private Map<Integer, Long> buildRatingFacets(List<UUID> productIds) {
        Map<Integer, Long> map = new LinkedHashMap<>();
        productRepository.findRatingFacetsByProductIds(productIds)
                .forEach(row -> map.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue()));
        return map;
    }

    /**
     * Aggregates spec key→values from product-level AND variant-level specs
     * of the current page products — both JSONB fields are covered.
     * e.g. { "color": ["Black","Gold","White"], "storage": ["128GB","256GB"] }
     */
    private Map<String, List<String>> buildSpecificationFacets(List<Product> products) {
        Map<String, Set<String>> specMap = new LinkedHashMap<>();

        for (Product p : products) {
            // product-level specs
            if (p.getSpecifications() != null) {
                p.getSpecifications().forEach((k, v) ->
                        specMap.computeIfAbsent(k, x -> new LinkedHashSet<>()).add(v));
            }
            // variant-level specs
            if (p.getProductVariants() != null) {
                p.getProductVariants().forEach(variant -> {
                    if (variant.getSpecifications() != null) {
                        variant.getSpecifications().forEach((k, v) ->
                                specMap.computeIfAbsent(k, x -> new LinkedHashSet<>()).add(v));
                    }
                });
            }
        }

        // Convert Set → sorted List for deterministic, consistent response
        Map<String, List<String>> result = new LinkedHashMap<>();
        specMap.forEach((k, v) -> result.put(k, v.stream().sorted().collect(Collectors.toList())));
        return result;
    }

    // -----------------------------------------------------------------------
    // PRIVATE SORT / VALIDATION HELPERS
    // -----------------------------------------------------------------------

    /**
     * Builds a Sort — only allows whitelisted product-level fields.
     * Variant-level fields (sellingPrice, discountPercentage) are handled
     * inside ProductSpecification; we fall back to createdAt here to keep
     * JPA Pageable safe.
     */
    private Sort buildSort(String sortBy, String sortDir) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        String field = ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : "createdAt";
        return Sort.by(direction, field);
    }

    private String[] validateSortArray(String[] sort) {
        return (sort == null || sort.length < 2) ? new String[]{"createdAt", "desc"} : sort;
    }

    private String generateUniqueSlug(String title, String currentSlug) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-");

        String candidate = baseSlug;
        int counter = 1;

        while (productRepository.existsBySlug(candidate) && !candidate.equals(currentSlug)) {
            candidate = baseSlug + "-" + counter++;
            if (counter > 100) {
                throw new IllegalArgumentException("Unable to generate unique slug for: " + title);
            }
        }
        return candidate;
    }
}

