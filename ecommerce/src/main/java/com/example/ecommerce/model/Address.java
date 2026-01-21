package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseModel {
    private String address;

    @Column(nullable = false)
    private String city;

    private String state;

    private String postalCode;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String mobile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @OneToOne
    @JsonBackReference
    private Seller seller;

}
