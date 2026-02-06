package com.example.ecommerce.dto.request;

import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.domain.UserRole;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.BankDetails;
import com.example.ecommerce.model.BusinessDetails;
import com.example.ecommerce.model.Transaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerCreateDTO {
    private String email;
    private AddressCreateRequestDTO address;
    private String sellerName;
    private String mobile;
    private String password;
    private BusinessDetails businessDetails;
    private BankDetails bankDetails;
    private String GSTIN;

}
