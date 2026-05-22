package com.devotee.resume.init.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // GENERATE TOKEN
    public String generateToken(String userId) {

        Date now = new Date();

        Date expiryDate =
                new Date(
                        now.getTime() + jwtExpiration
                );

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    // SECRET KEY
    private Key getSignInKey() {

        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes()
        );
    }

    // EXTRACT USER ID
    public String getUserIdFromToken(
            String token
    ) {

        Claims claims =
                Jwts.parserBuilder()
                        .setSigningKey(getSignInKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

        return claims.getSubject();
    }

    // VALIDATE TOKEN
    public boolean validateToken(
            String token
    ) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // CHECK TOKEN EXPIRY
    public boolean isTokenExpired(
            String token
    ) {

        try {

            Claims claims =
                    Jwts.parserBuilder()
                            .setSigningKey(getSignInKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

            return claims
                    .getExpiration()
                    .before(new Date());

        } catch (Exception e) {

            return true;
        }
    }
}