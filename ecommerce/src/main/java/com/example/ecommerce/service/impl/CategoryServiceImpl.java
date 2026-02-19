package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.reponse.CategoryResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.CategoryCreateRequestDTO;
import com.example.ecommerce.dto.request.CategoryUpdateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.interfaces.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    //Allow only for ROLE_SELLER
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryResponseDTO createCategory(CategoryCreateRequestDTO requestDTO) {
        Category category = CategoryMapper.toEntity(requestDTO);
        if(requestDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(UUID.fromString(requestDTO.getParentCategoryId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + requestDTO.getParentCategoryId()));
            category.setParent(parentCategory);
            category.setLevel(parentCategory.getLevel() + 1);
        }

        category.setSlug(generateUniqueSlug(requestDTO.getName(), null));

        var createdCategory= categoryRepository.save(category);
        return CategoryMapper.toDto(createdCategory);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO requestDTO) {
        Category existingCategory = categoryRepository.findById(UUID.fromString(requestDTO.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getId()));

        // Update name and slug if name is provided
        if(requestDTO.getName() != null && !requestDTO.getName().trim().isEmpty()) {
            existingCategory.setName(requestDTO.getName());

                  // Trim dashes from ends
            existingCategory.setSlug(generateUniqueSlug(requestDTO.getName(),existingCategory.getSlug()));
        }

        // Update parent category if provided
        if(requestDTO.getParentCategoryId() != null) {
            if(existingCategory.getId().equals(UUID.fromString(requestDTO.getParentCategoryId()))) {
                throw new IllegalArgumentException("A category cannot be its own parent.");
            }
            Category parentCategory = categoryRepository.findById(UUID.fromString(requestDTO.getParentCategoryId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + requestDTO.getParentCategoryId()));
            existingCategory.setParent(parentCategory);
            existingCategory.setLevel(parentCategory.getLevel() + 1);
        }

        var updatedCategory= categoryRepository.save(existingCategory);
        return CategoryMapper.toDto(updatedCategory);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(String categoryId) {
        Category existingCategory = categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        categoryRepository.delete(existingCategory);
    }


    /// ///Allow for all

    @Override
    public CategoryResponseDTO getCategoryById(String categoryId) {
        Category existingCategory = categoryRepository.findById(UUID.fromString(categoryId))
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return CategoryMapper.toDto(existingCategory);
    }

    @Override
    public Optional<Category> getCategoryEntityById(String categoryId) {
        return categoryRepository.findById(UUID.fromString(categoryId));
    }

    @Override
    public PageResponseDTO<CategoryResponseDTO> getAllCategories(String search, int page, int size, String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Category> currentPage;
        int currentPageNumber=pageable.getPageNumber();
        List<CategoryResponseDTO> contests;
        if (!Objects.equals(search, "") && search != null) {
            currentPage =categoryRepository.findBySearchKey(search, pageable);
        } else {
            currentPage = categoryRepository.findAll(pageable);
        }
        contests = (currentPage.getContent()).stream().map(CategoryMapper::toDto).toList();

        LOGGER.info("Retrieved {} contests", contests.size());

        return new PageResponseDTO<CategoryResponseDTO>(currentPageNumber,currentPage.getTotalPages(), contests);
    }

    @Override
    public List<CategoryResponseDTO> getSubCategories(String parentSlug) {
        List<Category> childCategories = categoryRepository.findByParentSlug(parentSlug);
        return childCategories.stream().map(CategoryMapper::toDto).toList();
    }

    @Override
    public CategoryResponseDTO getBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return CategoryMapper.toDto(category);
    }

    private String generateUniqueSlug(String title, String currentSlug) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // keep only letters, digits, spaces, hyphens
                .trim()
                .replaceAll("[\\s-]+", "-");        // collapse spaces/hyphens into one dash

        String candidate = baseSlug;
        int counter = 1;

        while (categoryRepository.existsBySlug(candidate)
                && !candidate.equals(currentSlug)) {
            candidate = baseSlug + "-" + counter++;
        }
        return candidate;
    }


}
