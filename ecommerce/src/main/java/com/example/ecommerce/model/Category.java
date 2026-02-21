package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_category_slug", columnList = "slug" ,unique = true)
        }
)
public class Category  extends  BaseModel{

    private String name;

    @Column(nullable = false,unique = true)
    private String slug;

    private String imgUrl;

    @ManyToOne
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false)
    private int level=1;


}
