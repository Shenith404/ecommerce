package com.example.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

}
