package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.StoreResponseDTO;
import com.example.ecommerce.dto.request.StoreCreateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.StoreMapper;
import com.example.ecommerce.model.Store;
import com.example.ecommerce.repository.StoreRepository;
import com.example.ecommerce.service.interfaces.FileUploadService;
import com.example.ecommerce.service.interfaces.SellerService;
import com.example.ecommerce.service.interfaces.StoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.ecommerce.utils.UuidUtil.parseUUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {


    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;
    private final JwtProvider jwtProvider;
    private final SellerService sellerService;
    private final FileUploadService fileUploadService;

    @Transactional
    @Override
    public StoreResponseDTO create(StoreCreateRequestDTO dto, MultipartFile logo, MultipartFile banner) throws IOException {
        Store newStore= StoreMapper.toEntity(dto);
        //get seller from token
        String sellerEmail = jwtProvider.getEmailFromHeader();
        var Owner = sellerService.getSellerEntityByEmail(sellerEmail)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Seller is Not found with email")
                );
        newStore.setOwner(Owner);
        //upload logo and banner
        if(logo != null && !logo.isEmpty()) {
            String logoUrl = fileUploadService.uploadImage(logo);
            newStore.setLogoUrl(logoUrl);
        }
        if(banner != null && !banner.isEmpty()) {
            String bannerUrl = fileUploadService.uploadImage(banner);
            newStore.setBannerUrl(bannerUrl);
        }
        String uniqueSlug = generateUniqueSlug(dto.getStoreName(), null);
        newStore.setSeoSlug(uniqueSlug);
        Store savedStore = storeRepository.save(newStore);
        LOGGER.info("Store created successfully");
        return StoreMapper.toDto(savedStore);
    }

    @Transactional
    @Override
    public StoreResponseDTO updateDetails(String storeId, StoreCreateRequestDTO dto) {
        var existingStore = storeRepository.findById(parseUUID(storeId, "Store"))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Store is Not found with id: " + storeId)
                );
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!existingStore.getOwner().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this store");
        }
        if(dto.getStoreName() != null){
            existingStore.setStoreName(dto.getStoreName());
            String uniqueSlug = generateUniqueSlug(dto.getStoreName(), existingStore.getSeoSlug());
            existingStore.setSeoSlug(uniqueSlug);
        }
        if(dto.getDescription() != null){
            existingStore.setDescription(dto.getDescription());
        }
        if(dto.getReturnPolicy() != null){
            existingStore.setReturnPolicy(dto.getReturnPolicy());
        }
        Store updatedStore = storeRepository.save(existingStore);
        LOGGER.info("Store updated successfully");
        return StoreMapper.toDto(updatedStore);
    }

    @Transactional
    @Override
    public StoreResponseDTO updateLogo(String storeId, MultipartFile logo) throws IOException {
        var store = storeRepository.findById(parseUUID(storeId, "Store"))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Store is not found with id: " + storeId)
                );
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!store.getOwner().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this store");
        }
        if (logo.isEmpty()) {
            throw new IllegalArgumentException("Logo file is empty");
        }
        //delete existing image
        if(store.getLogoUrl() != null) {
            fileUploadService.deleteImage(store.getLogoUrl());
        }
        String logoUrl = fileUploadService.uploadImage(logo);
        store.setLogoUrl(logoUrl);
        Store updatedStore = storeRepository.save(store);
        LOGGER.info("Store logo updated successfully");
        return StoreMapper.toDto(updatedStore);
    }

    @Transactional
    @Override
    public StoreResponseDTO updateBanner(String storeId, MultipartFile banner) throws IOException {
        var store = storeRepository.findById(parseUUID(storeId, "Store"))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Store is not found with id: " + storeId)
                );
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!store.getOwner().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to update this store");
        }
        if (banner.isEmpty()) {
            throw new IllegalArgumentException("Banner file is empty");
        }
        //delete existing image
        if(store.getBannerUrl() != null) {
            fileUploadService.deleteImage(store.getBannerUrl());
        }
        String bannerUrl = fileUploadService.uploadImage(banner);
        store.setBannerUrl(bannerUrl);
        Store updatedStore = storeRepository.save(store);
        LOGGER.info("Store Banner updated successfully");
        return StoreMapper.toDto(updatedStore);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Store> getStoreEntityById(String storeId) {
        return storeRepository.findById(UUID.fromString(storeId));
    }

    @Override
    public StoreResponseDTO findBySeoSlug(String seoSlug) {
        Store existingStore = storeRepository.findBySeoSlug(seoSlug)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Store not found with slug: " + seoSlug)
                );
        return StoreMapper.toDto(existingStore);
    }

    @Transactional(readOnly = true)
    @Override
    public StoreResponseDTO getById(String storeId) {
        var store = storeRepository.findById(parseUUID(storeId, "Store"))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Store not found with id: " + storeId)
                );
        return StoreMapper.toDto(store);
    }

    @Transactional
    @Override
    public void delete(String storeId) {
        var store = storeRepository.findById(parseUUID(storeId, "Store"))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Store is not found with id: " + storeId)
                );
        String sellerEmail = jwtProvider.getEmailFromHeader();
        if (!store.getOwner().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("Unauthorized to delete this store");
        }
        LOGGER.info("Store Deleted Successfully");
        storeRepository.delete(store);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<StoreResponseDTO> getAll(String search, int page, int size, String[] sort) {
        // Validate and set default sort
        if (sort == null || sort.length < 2) {
            sort = new String[]{"storeName", "asc"};
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Store> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<StoreResponseDTO> stores;

        if (search != null && !search.isBlank()) {
            currentPage = storeRepository.findBySearchKey(search.trim(), pageable);
        } else {
            currentPage = storeRepository.findAll(pageable);
        }

        stores = currentPage.getContent().stream()
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());

        LOGGER.info("Retrieved {} stores key(s)", stores.size());

        return new PageResponseDTO<StoreResponseDTO>(currentPageNumber, currentPage.getTotalPages(), stores);
    }



    private String generateUniqueSlug(String title, String currentSlug) {
        String baseSlug = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // keep only letters, digits, spaces, hyphens
                .trim()
                .replaceAll("[\\s-]+", "-");        // collapse spaces/hyphens into one dash

        String candidate = baseSlug;
        int counter = 1;

        while (storeRepository.existsBySeoSlug(candidate)
                && !candidate.equals(currentSlug)) {
            candidate = baseSlug + "-" + counter++;
            if(counter > 100) { // safety check to prevent infinite loop
                throw new IllegalArgumentException("Unable to generate unique slug for product title: " + title);
            }
        }
        return candidate;
    }


}
