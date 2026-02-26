package com.example.ecommerce.repository;

import com.example.ecommerce.model.SpecificationKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecificationKeyRepository extends JpaRepository<SpecificationKey, UUID> {
    boolean existsByName(String name);
    
    Optional<SpecificationKey> findByName(String name);
    
    Optional<SpecificationKey> findByNameIgnoreCase(String name);
    
    @Query("SELECT s FROM SpecificationKey s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<SpecificationKey> findBySearchKey(String search, Pageable pageable);
}
