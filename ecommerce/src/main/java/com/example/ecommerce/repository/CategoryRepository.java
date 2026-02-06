package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM Category c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.slug) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Category> findBySearchKey(String search, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.parent.id =:uuid")
    List<Category> findByParentId(UUID uuid);
}
