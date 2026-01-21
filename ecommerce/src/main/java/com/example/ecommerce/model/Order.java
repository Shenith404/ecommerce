package com.example.ecommerce.model;

import com.example.ecommerce.domain.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseModel{
    @Column(nullable = false, unique = true)
    private String  orderId;

    @ManyToOne
    @JsonBackReference
    private User user;

    private UUID sellerID;

    @OneToMany(mappedBy = "order",cascade= CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems= new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Address shippingAddress;

    @Embedded
    private PaymentDetails paymentDetails;

    private double totalMrpPrice;

    private double totalSellingPrice;

    private double discount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalItems;



    private OffsetDateTime orderDate = OffsetDateTime.now();

    private OffsetDateTime shippingDate = OffsetDateTime.now().plusDays(7);

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Transaction transaction;

    @ManyToOne
    @JsonBackReference
    private PaymentOrder paymentOrder;





}
