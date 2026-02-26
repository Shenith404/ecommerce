package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.PageResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationKeyResponseDTO;
import com.example.ecommerce.dto.reponse.SpecificationOptionResponseDTO;
import com.example.ecommerce.dto.request.SpecificationKeyCreateRequestDTO;
import com.example.ecommerce.dto.request.SpecificationKeyUpdateRequestDTO;
import com.example.ecommerce.dto.request.SpecificationOptionCreateRequestDTO;
import com.example.ecommerce.model.SpecificationKey;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SpecificationKeyService {
    
    SpecificationKeyResponseDTO createSpecificationKey(SpecificationKeyCreateRequestDTO dto);
    
    SpecificationKeyResponseDTO updateSpecificationKey(SpecificationKeyUpdateRequestDTO dto);
    
    void deleteSpecificationKey(String specKeyId);
    
    SpecificationKeyResponseDTO getSpecificationKeyById(String specKeyId);
    
    Optional<SpecificationKey> getSpecificationKeyEntityById(String specKeyId);
    
    List<SpecificationKey> getSpecificationKeyEntitiesByIds(Set<String> specKeyIds);
    
    PageResponseDTO<SpecificationKeyResponseDTO> getAllSpecificationKeys(String search, int page, int size, String[] sort);
    
    // Methods for managing options within a specification key
    List<SpecificationOptionResponseDTO> addOptionsToSpecificationKey(String specKeyId, Set<SpecificationOptionCreateRequestDTO> options);
    
    void removeOptionsFromSpecificationKey(String specKeyId, Set<String> optionIds);
    
    List<SpecificationOptionResponseDTO> getOptionsForSpecificationKey(String specKeyId);
}
