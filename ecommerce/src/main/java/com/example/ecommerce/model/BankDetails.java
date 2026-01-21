package com.example.ecommerce.model;

import jakarta.persistence.Embeddable;
import lombok.*;
@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails  {
    private String  accountHolderName;
    private String  accountNumber;
    private String ifscCode;

}
