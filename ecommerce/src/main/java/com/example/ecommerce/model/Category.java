package com.example.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category  extends  BaseModel{

    private String name;

    @Column(nullable = false,unique = true)
    private String categoryId;

    @ManyToOne
    private Category parent;

    @Column(nullable = false)
    private int level=1;



}
