package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.dto.reponse.AddressResponseDTO;
import com.example.ecommerce.dto.reponse.UserResponseDTO;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public UserResponseDTO findUserByJwtToken(String token) {
        String email = jwtProvider.getEmailFromToken(token);

        var User = userRepository.findByEmail(email)
                .orElseThrow(
                        ()->  new ResourceNotFoundException("User with email " + email + " not found")
                );

        return UserResponseDTO.builder()
                .id(User.getId().toString())
                .email(User.getEmail())
                .fullName(User.getFullName())
                .role(User.getRole().name())
                .createdAt(User.getCreatedAt().toString())
                .updatedAt(User.getUpdatedAt().toString())
                .addresses(User.getAddresses().stream().map(address -> AddressResponseDTO.builder()
                        .address(address.getAddress())
                        .city(address.getCity())
                        .state(address.getState())
                        .postalCode(address.getPostalCode())
                        .country(address.getCountry())
                        .mobile(address.getMobile())
                        .build()).toList())
                .build();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        ()->  new ResourceNotFoundException("User with email " + email + " not found")
                );
    }
}
