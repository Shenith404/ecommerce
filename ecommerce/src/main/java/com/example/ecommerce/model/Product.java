package com.example.ecommerce.model;

import com.example.ecommerce.domain.ItemCondition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( indexes = {
        @Index(name = "idx_product_slug", columnList = "seoSlug", unique = true),
        @Index(name = "idx_product_condition", columnList = "itemCondition")
})
public class Product  extends BaseModel{

    private String title;

    @Column(unique = true)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)

    private ItemCondition itemCondition; // New, Used, Refurbished

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,String> specifications = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    private Brand brand;

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
