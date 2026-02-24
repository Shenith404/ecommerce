package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.dto.reponse.CategoryResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.CategoryCreateRequestDTO;
import com.example.ecommerce.dto.request.CategoryUpdateRequestDTO;
import com.example.ecommerce.model.Category;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    public CategoryResponseDTO createCategory(CategoryCreateRequestDTO requestDTO, MultipartFile image) throws IOException ;
    public CategoryResponseDTO updateCategoryDetails(CategoryUpdateRequestDTO requestDTO);
    CategoryResponseDTO updateCategoryImage(String categoryId, MultipartFile image) throws IOException;
    public void deleteCategory(String categoryId);
    public void addBrandsToCategory(String categoryId, Set<String> brandIds);
    public void removeBrandsFromCategory(String categoryId, Set<String> brandIds);
    public List<BrandResponseDTO> getBrandsForCategory(String categoryId);
    public CategoryResponseDTO getCategoryById(String categoryId);
    public Optional<Category> getCategoryEntityById(String categoryId);
    public PageResponseDTO<CategoryResponseDTO> getAllCategories(String search, int page, int size, String[] sort);
    public List<CategoryResponseDTO> getSubCategories(String parentSlug);
    public CategoryResponseDTO getBySlug(String slug);
}
