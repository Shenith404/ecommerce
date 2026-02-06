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

    @Override
    public CategoryResponseDTO createCategory(CategoryCreateRequestDTO requestDTO) {
        Category category = CategoryMapper.toEntity(requestDTO);
        if(requestDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(UUID.fromString(requestDTO.getParentCategoryId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + requestDTO.getParentCategoryId()));
            category.setParent(parentCategory);
            category.setLevel(parentCategory.getLevel() + 1);
        }

        String baseSlug = category.getName().toLowerCase()
                .replaceAll("[^a-z0-9]", "-") // Remove special chars
                .replaceAll("-+", "-")        // Remove double dashes
                .replaceAll("^-|-$", "")
                .replaceAll(" ","-");     // Trim dashes from ends
        category.setSlug(baseSlug + "-" + UUID.randomUUID().toString().substring(0, 8)); // Add random suffix for uniqueness


        var createdCategory= categoryRepository.save(category);
        return CategoryMapper.toDto(createdCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO requestDTO) {
        Category existingCategory = categoryRepository.findById(UUID.fromString(requestDTO.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getId()));

        // Update name and slug if name is provided
        if(requestDTO.getName() != null && !requestDTO.getName().trim().isEmpty()) {
            existingCategory.setName(requestDTO.getName());

            // Generate new slug
            String baseSlug = requestDTO.getName().toLowerCase()
                    .replaceAll("[^a-z0-9]", "-") // Remove special chars
                    .replaceAll("-+", "-")        // Remove double dashes
                    .replaceAll("^-|-$", "")
                    .replaceAll(" ","-");         // Trim dashes from ends
            existingCategory.setSlug(baseSlug + "-" + UUID.randomUUID().toString().substring(0, 8));
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
    public void deleteCategory(String categoryId) {
        Category existingCategory = categoryRepository.findById(UUID.fromString(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        categoryRepository.delete(existingCategory);
    }

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
    public List<CategoryResponseDTO> getSubCategories(String parentCategoryId) {
        List<Category> childCategories = categoryRepository.findByParentId(UUID.fromString(parentCategoryId));
        return childCategories.stream().map(CategoryMapper::toDto).toList();
    }


}
