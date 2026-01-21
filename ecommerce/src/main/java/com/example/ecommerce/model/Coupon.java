package com.example.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon extends BaseModel {

    @Column(nullable = false)
    private String code;

    private String description;

    private double discountPercentage;

    @Column(nullable = false)
    private OffsetDateTime validityStartDate;

    @Column(nullable = false)
    private OffsetDateTime validityEndDate;

    @Column(nullable = false)
    private double minimumOrderValue;

    @Column(nullable = false)
    private boolean isActive= true;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<User> users;
}
