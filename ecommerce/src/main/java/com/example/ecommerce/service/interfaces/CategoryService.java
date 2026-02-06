package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.CategoryResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.CategoryCreateRequestDTO;
import com.example.ecommerce.dto.request.CategoryUpdateRequestDTO;
import com.example.ecommerce.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public CategoryResponseDTO createCategory(CategoryCreateRequestDTO requestDTO);
    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO requestDTO);
    public void deleteCategory(String categoryId);
    public CategoryResponseDTO getCategoryById(String categoryId);
    public Optional<Category> getCategoryEntityById(String categoryId);
    public PageResponseDTO<CategoryResponseDTO> getAllCategories(String search, int page, int size, String[] sort);
    public List<CategoryResponseDTO> getSubCategories(String parentCategoryId);
}
