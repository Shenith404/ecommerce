package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.BrandResponseDTO;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.request.BrandCreateRequestDTO;
import com.example.ecommerce.model.Brand;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BrandService {

    BrandResponseDTO createBrand(BrandCreateRequestDTO dto, MultipartFile image) throws IOException;

    BrandResponseDTO updateBrandDetails(String brandId, BrandCreateRequestDTO dto);

    BrandResponseDTO updateBrandLogo(String brandId, MultipartFile image) throws IOException;

    void deleteBrand(String brandId);

    BrandResponseDTO getBrandBySlug(String slug);

    Optional<Brand> getBrandEntityById(String brandId);

    List<Brand> getBrandEntitiesByIds(Set<String> brandIds);

    PageResponseDTO<BrandResponseDTO> getAllBrands(String search, int page, int size, String[] sort);
}

