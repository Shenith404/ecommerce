package com.example.ecommerce.model;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_seller_email", columnList = "email" ,unique = true)
        }
)
public class Seller extends BaseModel {

    private String sellerName;

    private String mobile;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    //Instead of creating separate business_details and bank_details tables, all fields from BusinessDetails and BankDetails classes will be stored as columns directly in the Seller table.

    @Embedded
    private BusinessDetails businessDetails;

    @Embedded
    private BankDetails bankDetails;

    @OneToOne(mappedBy = "seller", orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address pickupAddress;

    private String GSTIN;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_SELLER;

    private boolean isEmailVerified = false;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;


    @OneToMany(mappedBy = "seller",cascade=CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Set<Transaction> transactions = new HashSet<>();

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Store store;


}
