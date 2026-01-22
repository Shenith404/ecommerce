package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.reponse.UserResponseDTO;
import com.example.ecommerce.model.User;

public interface UserService {

    public UserResponseDTO findUserByJwtToken(String token);
    public User findUserByEmail(String email);
}
