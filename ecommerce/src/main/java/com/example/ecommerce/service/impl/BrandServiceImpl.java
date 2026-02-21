package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.BrandCreateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.BrandMapper;
import com.example.ecommerce.model.Brand;
import com.example.ecommerce.repository.BrandRepository;
import com.example.ecommerce.service.interfaces.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements com.example.ecommerce.service.interfaces.BrandService {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;
    private final FileUploadService fileUploadService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public BrandResponseDTO createBrand(BrandCreateRequestDTO dto, MultipartFile image) throws IOException {

        String logoUrl = fileUploadService.uploadImage(image);
        String uniqueSlug = generateUniqueSlug(dto.getName(), null);

        Brand newBrand = Brand.builder()
                .name(dto.getName())
                .seoSlug(uniqueSlug)
                .logoUrl(logoUrl)
                .build();
        var createdBrand = brandRepository.save(newBrand);
        LOGGER.info("Brand created successfully");
        return BrandMapper.toDto(createdBrand);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public BrandResponseDTO updateBrandDetails(String brandId, BrandCreateRequestDTO dto) {
        var existingBrand = brandRepository.findById(UUID.fromString(brandId))
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        String uniqueSlug = generateUniqueSlug(dto.getName(), existingBrand.getSeoSlug());

        existingBrand.setName(dto.getName());
        existingBrand.setSeoSlug(uniqueSlug);

        var updatedBrand = brandRepository.save(existingBrand);
        LOGGER.info("Brand updated successfully");
        return BrandMapper.toDto(updatedBrand);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public BrandResponseDTO updateBrandLogo(String brandId, MultipartFile image) throws IOException {
        var existingBrand = brandRepository.findById(UUID.fromString(brandId))
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        String logoUrl = fileUploadService.uploadImage(image);
        existingBrand.setLogoUrl(logoUrl);

        var updatedBrand = brandRepository.save(existingBrand);
        LOGGER.info("Brand logo updated successfully");
        return BrandMapper.toDto(updatedBrand);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteBrand(String brandId) {
        var existingBrand = brandRepository.findById(UUID.fromString(brandId))
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        brandRepository.delete(existingBrand);
        LOGGER.info("Brand deleted successfully");
    }

    //get brand by slug
    @Transactional(readOnly = true)
    public BrandResponseDTO getBrandBySlug(String slug) {
        Brand brand = brandRepository.findBySlug(slug).
                orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        return BrandMapper.toDto(brand);
    }

    //get all
    @Transactional(readOnly = true)
    public PageResponseDTO<BrandResponseDTO> getAllBrands(String search, int page, int size, String[] sort) {
        // Validate sort array
        if (sort == null || sort.length < 2) {
            sort = new String[]{"createdAt", "desc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Brand> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<BrandResponseDTO> brands;
        if (!Objects.equals(search, "") && search != null) {
            currentPage = brandRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = brandRepository.findAll(pageable);
        }
        brands = (currentPage.getContent()).stream().map(BrandMapper::toDto).toList();

        LOGGER.info("Retrieved {} brand", brands.size());

        return new PageResponseDTO<BrandResponseDTO>(currentPageNumber, currentPage.getTotalPages(), brands);
    }






    private String generateUniqueSlug(String title, String currentSlug) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // keep only letters, digits, spaces, hyphens
                .trim()
                .replaceAll("[\\s-]+", "-");        // collapse spaces/hyphens into one dash

        String candidate = baseSlug;
        int counter = 1;

        while (brandRepository.existsBySlug(candidate)
                && !candidate.equals(currentSlug)) {
            candidate = baseSlug + "-" + counter++;
        }
        return candidate;
    }
}
