package com.example.ecommerce.service.interfaces;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(String userEmail, String otp, String subject, String text) throws MessagingException;
}
