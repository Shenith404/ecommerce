package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationOption extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_key_id", nullable = false)
    private SpecificationKey specificationKey;

    @Column(nullable = false)
    private String value; // e.g., "16GB", "Cotton"
}
