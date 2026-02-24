package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.CategoryResponseDTO;
import com.example.ecommerce.dto.request.CategoryCreateRequestDTO;
import com.example.ecommerce.model.Category;

public class CategoryMapper {
    public static CategoryResponseDTO toDto(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId().toString())
                .name(category.getName())
                .slug(category.getSlug())
                .parentCategoryId(category.getParent() != null ?  category.getParent().getId().toString() : null)
                .level(String.valueOf(category.getLevel()))
                .imgUrl(category.getImgUrl()==null ? null : category.getImgUrl())
                .build();
    }
    public static Category toEntity(CategoryCreateRequestDTO requestDTO) {
        Category category = new Category();
        category.setName(requestDTO.getName());
        return category;
    }
}
