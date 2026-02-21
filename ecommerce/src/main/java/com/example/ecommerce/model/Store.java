package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stores", indexes = {@Index(name = "idx_store_slug", columnList = "seoSlug", unique = true)})
public class Store extends BaseModel {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Seller owner;

    @Column(nullable = false, unique = true)
    private String storeName;

    @Column(nullable = false, unique = true)
    private String seoSlug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logoUrl;

    private String bannerUrl;

    @Column(columnDefinition = "TEXT")
    private String returnPolicy;

    private double averageRating = 0.0;
}