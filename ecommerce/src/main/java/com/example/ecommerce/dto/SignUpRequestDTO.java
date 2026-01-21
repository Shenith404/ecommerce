package com.example.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestDTO {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "OTP is required")
    private String otp;
}
