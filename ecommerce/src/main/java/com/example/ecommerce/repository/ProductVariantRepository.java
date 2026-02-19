package com.example.ecommerce.repository;

import com.example.ecommerce.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
    List<ProductVariant> findByProductId(UUID productId);


    @Modifying
    @Query("UPDATE ProductVariant v SET v.stockQuantity = v.stockQuantity + :amount WHERE v.id = :variantId")
    void addStock(@Param("variantId") UUID variantId, @Param("amount") int amount);

    // Atomic query to REMOVE stock safely (Prevents negative stock!)
    @Modifying
    @Query("UPDATE ProductVariant v SET v.stockQuantity = v.stockQuantity - :amount WHERE v.id = :variantId AND v.stockQuantity >= :amount")
    int removeStock(@Param("variantId") UUID variantId, @Param("amount") int amount);

}
