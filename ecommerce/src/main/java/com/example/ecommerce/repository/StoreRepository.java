package com.example.ecommerce.repository;

import com.example.ecommerce.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    boolean existsBySeoSlug(String seoSlug);

    @Query("SELECT s FROM Store s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchKey, '%'))")
    Page<Store> findBySearchKey(String trim, Pageable pageable);

    Optional<Store> findBySeoSlug(String seoSlug);
}
