package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.domain.UserRole;
import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.dto.reponse.AuthResponseDTO;
import com.example.ecommerce.dto.request.CartCreateRequest;
import com.example.ecommerce.dto.request.LoginRequestDTO;
import com.example.ecommerce.dto.request.SignUpRequestDTO;
import com.example.ecommerce.dto.request.VerificationCodeRequestDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.VerificationCode;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.interfaces.*;
import com.example.ecommerce.utils.AppUtil;
import com.example.ecommerce.utils.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final JwtProvider jwtProvider;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;

    @Transactional
    @Override
    public ApiResponseDTO<Void> createUser(SignUpRequestDTO signUpRequestDTO) {

        var user = userRepository.findByEmail(signUpRequestDTO.getEmail());
        if (user.isPresent()) {
            throw new ResourceNotFoundException("User already exists with email: " + signUpRequestDTO.getEmail());
        }

        User newUser = new User();
        newUser.setFullName(signUpRequestDTO.getFullName());
        newUser.setEmail(signUpRequestDTO.getEmail());
        newUser.setRole(UserRole.ROLE_CUSTOMER);
        newUser = userRepository.save(newUser);
        //create cart for user
        cartService.CreateCart(new CartCreateRequest(newUser));
        LOGGER.info("New user created with email: {}", signUpRequestDTO.getEmail());

        return ApiResponseDTO.<Void>builder()
                .data(null)
                .message("User created successfully")
                .success(true)
                .build();
    }

    @Override
    public ApiResponseDTO<AuthResponseDTO> loginUser(LoginRequestDTO loginRequestDTO){
        String username = loginRequestDTO.getEmail();
        String otp = loginRequestDTO.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        return ApiResponseDTO.<AuthResponseDTO>builder()
                .data(AuthResponseDTO.builder()
                        .token(token)
                        .role(roleName)
                        .build())
                .message("User logged in successfully")
                .success(true)
                .build();
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        VerificationCode verificationCode = verificationCodeService.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Verification code not found for email: " + username));
        if(!verificationCode.getOtp().equals(otp)) {
            throw new ResourceNotFoundException("Invalid OTP code for email: " + username);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public void sendLoginOtp(VerificationCodeRequestDTO requestDTO) throws MessagingException {

        String SIGNIN_PREFIX = "signin_";
        String email = requestDTO.getEmail();

        if (email.startsWith(SIGNIN_PREFIX)) {
            email = email.substring(SIGNIN_PREFIX.length());
        }
        if(requestDTO.getRole().equals(UserRole.ROLE_SELLER.toString())) {
            sellerRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found with email "));
        }else{
            System.out.println("Checking user email: " + email);
            userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email "));
        }

        Optional<VerificationCode> isExists = verificationCodeService.findByEmail(email);
        isExists.ifPresent(verificationCodeService::delete);
        //generate new OTP
        String otp= OtpUtil.generateOtp();
        LOGGER.info("OTP generated: {}", otp);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeService.create(verificationCode);

        String subject = AppUtil.APP_NAME + " login/Your Login OTP Code";
        String text  = "Your OTP code for login is: " + otp ;

        emailService.sendVerificationEmail(email, otp, subject , text);
        LOGGER.info("Send verification email to email: {}", email);

    }
}
