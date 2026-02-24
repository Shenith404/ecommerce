package com.example.ecommerce.repository;

import com.example.ecommerce.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    boolean existsBySeoSlug(String candidate);
    @Query("SELECT b FROM Brand b WHERE b.seoSlug = :candidate")
    Optional<Brand> findBySlug(String candidate);

    @Query("SELECT b FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(b.seoSlug) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Brand> findBySearchKey(String search, Pageable pageable);
}
