package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsBySlug(String slug);

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> findBySearchKey(String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.seller.email) = LOWER(:sellerEmail)")
    Page<Product> findAllSellerProducts(String sellerEmail,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND LOWER(p.seller.email) = LOWER(:sellerEmail)")
    Page<Product> findSellerProductsBySearchKey(String search,String sellerEmail ,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND p.category.slug = :categorySlug")
    Page<Product> findCategoryProductsBySearchKey(String categorySlug, String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE  p.category.slug = :categorySlug")
    Page<Product> findAllCategoryProducts(String categorySlug, Pageable pageable);
}
