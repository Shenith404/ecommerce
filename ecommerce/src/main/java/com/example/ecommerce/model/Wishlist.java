package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist  extends BaseModel {
    @OneToOne
    @JsonBackReference
    private User user;

    @ManyToMany
    @JsonBackReference
    private Set<Product> products= new HashSet<>();
}
