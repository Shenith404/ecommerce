package com.example.ecommerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String JWT_SECRET ;
    @Value("${jwt.expiration.time}")
    private int jwtExpirationInMs;

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles= populateAuthorities(authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", authentication.getName());
        claims.put("authorities", roles);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths= new HashSet<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            auths.add(grantedAuthority.getAuthority());
        }
        return String.join(",", auths);
    }

    public String getEmailFromToken(String token) {
        token= token.substring(7);
        Claims claims = Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload();
        return String.valueOf(claims.get("email"));
    }
    public String getEmailFromHeader(){
        try {
            // Get the current HTTP request
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();

            // Extract JWT token from Authorization header
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);

                Claims claims = Jwts.parser().verifyWith(getSigningKey()).build()
                        .parseSignedClaims(jwt).getPayload();
                String userId = String.valueOf(claims.get("email"));;

                if (userId != null) {
                    return userId;
                }
            }
        } catch (Exception e) {
        }

        // Fallback to authentication name if JWT extraction fails
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        return authentication.getName();
    }
}
