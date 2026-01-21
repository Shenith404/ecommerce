package com.example.ecommerce.model;

import com.example.ecommerce.domain.HomeCategorySection;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeCategory extends BaseModel {

    private String name;
    private String image;
    private String categoryId;
    @Enumerated(EnumType.STRING)
    private HomeCategorySection section;
    @OneToOne(mappedBy = "category", orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonManagedReference
    private Deal deal;
}
