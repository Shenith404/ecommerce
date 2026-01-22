package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.CartCreateRequest;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.service.interfaces.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl  implements CartService {
    private final CartRepository cartRepository;
    @Override
    public void CreateCart(CartCreateRequest cartCreateRequest) {
        Cart cart = new Cart();
        cart.setUser(cartCreateRequest.getUser());
        cartRepository.save(cart);
    }
}
