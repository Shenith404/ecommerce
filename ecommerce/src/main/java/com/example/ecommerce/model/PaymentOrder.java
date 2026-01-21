package com.example.ecommerce.model;
import com.example.ecommerce.domain.PaymentMethod;
import com.example.ecommerce.domain.PaymentOrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class PaymentOrder extends BaseModel
{
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String paymentLinkId;

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "paymentOrder",cascade=CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Set<Order> orders = new HashSet<>();

}
