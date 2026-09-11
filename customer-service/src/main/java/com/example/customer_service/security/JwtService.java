package com.example.customer_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:min-superhemliga-jwt-nyckel-som-ar-jattelang-123}")
    private String jwtSecret;

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(String email){
        long now = System.currentTimeMillis();
        long expiry = now + 1000 * 60 * 60;
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(now))
                .expiration(new Date(expiry))
                .signWith(getKey())
                .compact();
    }
}
