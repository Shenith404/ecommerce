package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.VerificationCodeRepository;
import com.example.ecommerce.service.interfaces.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public Optional<VerificationCode> findByEmail(String email) {
        return verificationCodeRepository.findByEmail(email);
    }

    @Override
    public void delete(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

    @Override
    public void create(VerificationCode verificationCode) {
        verificationCodeRepository.save(verificationCode);
    }
}
