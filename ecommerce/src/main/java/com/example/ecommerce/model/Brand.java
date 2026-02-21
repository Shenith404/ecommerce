package com.example.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands", indexes = {@Index(name = "idx_brand_slug", columnList = "seoSlug", unique = true)})
public class Brand extends BaseModel {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String seoSlug;

    private String logoUrl;
}