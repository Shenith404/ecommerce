package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.BrandCreateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BrandService {

    BrandResponseDTO createBrand(BrandCreateRequestDTO dto, MultipartFile image) throws IOException;

    BrandResponseDTO updateBrandDetails(String brandId, BrandCreateRequestDTO dto);

    BrandResponseDTO updateBrandLogo(String brandId, MultipartFile image) throws IOException;

    void deleteBrand(String brandId);

    BrandResponseDTO getBrandBySlug(String slug);

    PageResponseDTO<BrandResponseDTO> getAllBrands(String search, int page, int size, String[] sort);
}

