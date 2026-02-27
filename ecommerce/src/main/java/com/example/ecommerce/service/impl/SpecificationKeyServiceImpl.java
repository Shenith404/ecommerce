package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationKeyResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationOptionResponseDTO;
import com.example.ecommerce.dto.request.SpecificationKeyCreateRequestDTO;
import com.example.ecommerce.dto.request.SpecificationKeyUpdateRequestDTO;
import com.example.ecommerce.dto.request.SpecificationOptionCreateRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.mapper.SpecificationKeyMapper;
import com.example.ecommerce.mapper.SpecificationOptionMapper;
import com.example.ecommerce.model.SpecificationKey;
import com.example.ecommerce.model.SpecificationOption;
import com.example.ecommerce.repository.SpecificationKeyRepository;
import com.example.ecommerce.service.interfaces.SpecificationKeyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecificationKeyServiceImpl implements SpecificationKeyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationKeyServiceImpl.class);

    private final SpecificationKeyRepository specificationKeyRepository;

    /**
     * Safely parse UUID string with proper error handling
     */
    private UUID parseUUID(String id, String entityName) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + entityName + " ID format: " + id);
        }
    }

    @Transactional
    @Override
    public SpecificationKeyResponseDTO createSpecificationKey(SpecificationKeyCreateRequestDTO dto) {
        String trimmedName = dto.getName().trim();
        
        // Check if a specification key with the same name already exists (case-insensitive)
        if (specificationKeyRepository.findByNameIgnoreCase(trimmedName).isPresent()) {
            throw new IllegalArgumentException("Specification key with name '" + trimmedName + "' already exists");
        }

        SpecificationKey specKey = SpecificationKey.builder()
                .name(trimmedName)
                .isRequired(dto.isRequired())
                .build();

        var createdSpecKey = specificationKeyRepository.save(specKey);
        LOGGER.info("Specification key created successfully with ID: {}", createdSpecKey.getId());
        return SpecificationKeyMapper.toDto(createdSpecKey);
    }

    @Transactional
    @Override
    public SpecificationKeyResponseDTO updateSpecificationKey( String id,SpecificationKeyUpdateRequestDTO dto) {
        UUID specKeyId = parseUUID(id, "Specification Key");
        SpecificationKey existingSpecKey = specificationKeyRepository.findById(specKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Specification key not found with id: " + id));

        String trimmedName = dto.getName().trim();
        
        // Check if another specification key with the same name already exists (case-insensitive)
        if (!existingSpecKey.getName().equalsIgnoreCase(trimmedName)) {
            Optional<SpecificationKey> duplicate = specificationKeyRepository.findByNameIgnoreCase(trimmedName);
            if (duplicate.isPresent()) {
                throw new IllegalArgumentException("Specification key with name '" + trimmedName + "' already exists");
            }
        }

        existingSpecKey.setName(trimmedName);
        existingSpecKey.setRequired(dto.isRequired());

        var updatedSpecKey = specificationKeyRepository.save(existingSpecKey);
        LOGGER.info("Specification key updated successfully with ID: {}", updatedSpecKey.getId());
        return SpecificationKeyMapper.toDto(updatedSpecKey);
    }

    @Transactional
    @Override
    public void deleteSpecificationKey(String specKeyId) {
        UUID id = parseUUID(specKeyId, "Specification Key");
        SpecificationKey existingSpecKey = specificationKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specification key not found with id: " + specKeyId));
        
        specificationKeyRepository.delete(existingSpecKey);
        LOGGER.info("Specification key deleted successfully with ID: {}", specKeyId);
    }

    @Transactional(readOnly = true)
    @Override
    public SpecificationKeyResponseDTO getSpecificationKeyById(String specKeyId) {
        UUID id = parseUUID(specKeyId, "Specification Key");
        SpecificationKey specKey = specificationKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specification key not found with id: " + specKeyId));
        return SpecificationKeyMapper.toDto(specKey);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<SpecificationKey> getSpecificationKeyEntityById(String specKeyId) {
        UUID id = parseUUID(specKeyId, "Specification Key");
        return specificationKeyRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SpecificationKey> getSpecificationKeyEntitiesByIds(Set<String> specKeyIds) {
        Set<UUID> uuidSet = specKeyIds.stream()
                .map(id -> parseUUID(id, "Specification Key"))
                .collect(Collectors.toSet());
        
        List<SpecificationKey> found = specificationKeyRepository.findAllById(uuidSet);
        
        if (found.size() != specKeyIds.size()) {
            throw new ResourceNotFoundException("One or more specification keys not found");
        }
        
        return found;
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<SpecificationKeyResponseDTO> getAllSpecificationKeys(String search, int page, int size, String[] sort) {
        // Validate and set default sort
        if (sort == null || sort.length < 2) {
            sort = new String[]{"name", "asc"};
        }
        
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<SpecificationKey> currentPage;
        int currentPageNumber = pageable.getPageNumber();
        List<SpecificationKeyResponseDTO> specKeys;
        
        if (search != null && !search.isBlank()) {
            currentPage = specificationKeyRepository.findBySearchKey(search.trim(), pageable);
        } else {
            currentPage = specificationKeyRepository.findAll(pageable);
        }
        
        specKeys = currentPage.getContent().stream()
                .map(SpecificationKeyMapper::toDto)
                .collect(Collectors.toList());

        LOGGER.info("Retrieved {} specification key(s)", specKeys.size());

        return new PageResponseDTO<>(currentPageNumber, currentPage.getTotalPages(), specKeys);
    }

    @Transactional
    @Override
    public List<SpecificationOptionResponseDTO> addOptionsToSpecificationKey(String specKeyId, Set<SpecificationOptionCreateRequestDTO> options) {
        if (options == null || options.isEmpty()) {
            return new ArrayList<>();
        }

        UUID id = parseUUID(specKeyId, "Specification Key");
        SpecificationKey existingSpecKey = specificationKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specification key not found with id: " + specKeyId));

        Set<String> newValues = new HashSet<>();
        
        // Get existing option values (case-insensitive check)
        Set<String> existingValues = existingSpecKey.getOptions().stream()
                .map(opt -> opt.getValue().toLowerCase())
                .collect(Collectors.toSet());
        
        for (SpecificationOptionCreateRequestDTO optionDto : options) {
            String trimmedValue = optionDto.getValue().trim();
            
            // Check for duplicate (case-insensitive)
            if (existingValues.contains(trimmedValue.toLowerCase())) {
                LOGGER.warn("Option value '{}' already exists for Specification key ID: {}, skipping", trimmedValue, specKeyId);
                continue;
            }
            
            SpecificationOption option = SpecificationOption.builder()
                    .value(trimmedValue)
                    .specificationKey(existingSpecKey)
                    .build();
            existingSpecKey.getOptions().add(option);
            existingValues.add(trimmedValue.toLowerCase()); // Track newly added
            newValues.add(trimmedValue.toLowerCase());
        }

        if (!newValues.isEmpty()) {
            SpecificationKey savedSpecKey = specificationKeyRepository.save(existingSpecKey);
            LOGGER.info("Added {} option(s) to Specification key ID: {}", newValues.size(), specKeyId);
            
            // Return only the newly added options from the persisted entity
            return savedSpecKey.getOptions().stream()
                    .filter(opt -> newValues.contains(opt.getValue().toLowerCase()))
                    .map(SpecificationOptionMapper::toDto)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Transactional
    @Override
    public void removeOptionsFromSpecificationKey(String specKeyId, Set<String> optionIds) {
        if (optionIds == null || optionIds.isEmpty()) {
            return;
        }

        UUID id = parseUUID(specKeyId, "Specification Key");
        SpecificationKey existingSpecKey = specificationKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specification key not found with id: " + specKeyId));

        Set<UUID> optionUUIDs = optionIds.stream()
                .map(optId -> parseUUID(optId, "Option"))
                .collect(Collectors.toSet());

        int before = existingSpecKey.getOptions().size();
        existingSpecKey.getOptions().removeIf(option -> optionUUIDs.contains(option.getId()));
        int removedCount = before - existingSpecKey.getOptions().size();

        if (removedCount > 0) {
            specificationKeyRepository.save(existingSpecKey);
            LOGGER.info("Removed {} option(s) from Specification key ID: {}", removedCount, specKeyId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SpecificationOptionResponseDTO> getOptionsForSpecificationKey(String specKeyId) {
        UUID id = parseUUID(specKeyId, "Specification Key");
        SpecificationKey specKey = specificationKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specification key not found with id: " + specKeyId));

        return specKey.getOptions().stream()
                .map(SpecificationOptionMapper::toDto)
                .collect(Collectors.toList());
    }
}
