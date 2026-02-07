package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.VerificationCodeRepository;
import com.example.ecommerce.service.interfaces.VerificationCodeService;
import com.example.ecommerce.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public Optional<VerificationCode> findByEmail(String email) {
        if(email.startsWith(AppUtil.SELLER_PREFIX)){
            email=email.substring(AppUtil.SELLER_PREFIX.length());
        }
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
