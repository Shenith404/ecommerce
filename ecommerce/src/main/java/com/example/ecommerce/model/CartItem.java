package com.example.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem extends  BaseModel {

    @ManyToOne
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JsonBackReference
    private Product product;

    private String size;

    private int quantity=1;

    private double mrpPrice;

    private double sellingPrice;

    private UUID userId;

}
