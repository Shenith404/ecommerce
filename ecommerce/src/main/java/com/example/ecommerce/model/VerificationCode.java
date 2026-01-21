package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode extends BaseModel {
    private String otp;

    private String email;

    @OneToOne
    @JsonBackReference
    private User user;

    @OneToOne
    @JsonBackReference
    private Seller seller;
}
