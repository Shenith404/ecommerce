package com.example.ecommerce.service.impl;

import com.example.ecommerce.service.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaEmailSender;

    @Override
    public void sendVerificationEmail(String userEmail, String otp, String subject, String text) throws MessagingException {
        // Implementation for sending email
        MimeMessage mimeMessage = javaEmailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setSubject(subject);
        helper.setTo(userEmail);
        helper.setText(text, true); // true indicates HTML
        javaEmailSender.send(mimeMessage);

    }
}
