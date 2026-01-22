package com.example.ecommerce.service.interfaces;

import com.example.ecommerce.dto.request.CartCreateRequest;
import com.example.ecommerce.dto.request.SignUpRequestDTO;

public interface CartService {
    public void CreateCart(CartCreateRequest cartCreateRequest);
}
