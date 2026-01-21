package com.example.ecommerce.model;

import com.example.ecommerce.domain.PaymentStatus;
import jakarta.persistence.Embeddable;
import lombok.Data;
@Embeddable
@Data
public class PaymentDetails {
    private String paymentId;

    private String razorpayPaymentLinkId;

    private String razorpayPaymentLinkReferenceId;

    private String razorpayPaymentLinkStatus;

    private String razorpayPaymentIdZWSP;

    private PaymentStatus paymentStatus;


}
