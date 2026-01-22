package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.model.VerificationCode;

import java.util.Optional;

public interface VerificationCodeService {
    Optional<VerificationCode> findByEmail(String email);

    void delete(VerificationCode isExists);

    void create(VerificationCode verificationCode);
}
