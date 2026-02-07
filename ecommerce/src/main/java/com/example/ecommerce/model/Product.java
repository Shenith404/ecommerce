package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product  extends BaseModel{

    private String title;

    @Column(length = 1000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "product")
    private Set<ProductVariant> productVariants= new HashSet<>();

    private int numRatings;

    private double averageRating;

    @ManyToOne
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JsonIgnore
    private Seller seller;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne
    @JsonManagedReference
    private Set<Wishlist> wishlists= new HashSet<>();

}
