package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands", indexes = {@Index(name = "idx_brand_slug", columnList = "seo_slug", unique = true)})
public class Brand extends BaseModel {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String seoSlug;

    @Column(length = 500)
    private String logoUrl;

    @ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
    private Set<Category> categories = new HashSet<>();

}