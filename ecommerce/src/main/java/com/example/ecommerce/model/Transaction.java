package com.example.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseModel {

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToOne
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JsonBackReference
    private Seller seller;
}
