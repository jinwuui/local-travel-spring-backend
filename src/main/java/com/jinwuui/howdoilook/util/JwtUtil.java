package com.jinwuui.howdoilook.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private final String secretString = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm=\n";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    private final long expirationTime = 1000 * 60 * 60 * 2; // 2시간
    private final long longExpirationTime = 1000 * 60 * 60 * 48; // 48시간

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();

    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + longExpirationTime))
                .signWith(secretKey)
                .compact();

    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    public boolean isTokenNotValid(String token, String email) {
        return !isTokenValid(token, email);
    }

    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
