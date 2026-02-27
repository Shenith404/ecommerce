package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsBySlug(String slug);

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> findBySearchKey(String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.seller.email) = LOWER(:sellerEmail)")
    Page<Product> findAllSellerProducts(String sellerEmail, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND LOWER(p.seller.email) = LOWER(:sellerEmail)")
    Page<Product> findSellerProductsBySearchKey(String search,String sellerEmail ,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND p.category.slug = :categorySlug")
    Page<Product> findCategoryProductsBySearchKey(String categorySlug, String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.slug = :categorySlug")
    Page<Product> findAllCategoryProducts(String categorySlug, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId")
    Page<Product> findAllProductsBySellerId(UUID sellerId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND p.seller.id = :sellerId")
    Page<Product> findProductsBySellerIdAndSearchKey(UUID sellerId, String search, Pageable pageable);


    @Query("SELECT p.brand.id, p.brand.name, p.brand.seoSlug, COUNT(p) " +
            "FROM Product p WHERE p.id IN :productIds AND p.brand IS NOT NULL " +
            "GROUP BY p.brand.id, p.brand.name, p.brand.seoSlug " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> findBrandFacetsByProductIds(List<UUID> productIds);

    @Query("SELECT MIN(v.sellingPrice), MAX(v.sellingPrice) " +
            "FROM ProductVariant v WHERE v.product.id IN :productIds")
    Object[] findPriceRangeByProductIds(List<UUID> productIds);

    @Query("SELECT p.itemCondition, COUNT(p) FROM Product p " +
            "WHERE p.id IN :productIds GROUP BY p.itemCondition")
    List<Object[]> findConditionFacetsByProductIds(List<UUID> productIds);

    @Query("SELECT FLOOR(p.averageRating), COUNT(p) FROM Product p " +
            "WHERE p.id IN :productIds AND p.averageRating > 0 " +
            "GROUP BY FLOOR(p.averageRating) ORDER BY FLOOR(p.averageRating) DESC")
    List<Object[]> findRatingFacetsByProductIds(List<UUID> productIds);
}



