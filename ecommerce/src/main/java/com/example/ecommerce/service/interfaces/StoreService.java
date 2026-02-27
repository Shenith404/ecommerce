package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.StoreResponseDTO;
import com.example.ecommerce.dto.request.StoreCreateRequestDTO;
import com.example.ecommerce.model.Store;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface StoreService {
    public StoreResponseDTO create(StoreCreateRequestDTO dto, MultipartFile logo, MultipartFile banner) throws IOException;
    public StoreResponseDTO updateDetails(String storeId, StoreCreateRequestDTO dto);
    public StoreResponseDTO updateLogo(String storeId, MultipartFile logo) throws IOException;
    public StoreResponseDTO updateBanner(String storeId, MultipartFile banner) throws IOException;
    public Optional<Store> getStoreEntityById(String storeId);
    public StoreResponseDTO findBySeoSlug(String seoSlug);
    public StoreResponseDTO getById(String storeId);
    public void delete(String storeId);
    public PageResponseDTO<StoreResponseDTO> getAll(String search, int page, int size, String[] sort);

}
