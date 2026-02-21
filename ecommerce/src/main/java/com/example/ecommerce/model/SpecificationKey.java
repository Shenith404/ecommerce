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
@Table(name = "specification_keys")
public class SpecificationKey extends BaseModel {
    @Column(nullable = false, unique = true)
    private String name; // e.g., "RAM", "Material"

    private boolean isRequired = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_specifications",
            joinColumns = @JoinColumn(name = "spec_key_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}