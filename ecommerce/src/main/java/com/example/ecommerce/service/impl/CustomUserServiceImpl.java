package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.UserRole;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(AppUtil.SELLER_PREFIX)){
            String actualUsername= username.substring(AppUtil.SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername).orElseThrow(
                    ()-> new UsernameNotFoundException(actualUsername)
            );
            return buildUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());

        }else{
            User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
            return buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());


        }
    }

    private UserDetails buildUserDetails(String email, String password, UserRole role) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+(role==null ?UserRole.ROLE_CUSTOMER : role).toString()));
        return new org.springframework.security.core.userdetails.User(email,password,authorities);
    }
}
