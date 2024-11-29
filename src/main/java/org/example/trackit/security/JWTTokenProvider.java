package org.example.trackit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JWTTokenProvider {

    @Value("${jwt_secret}")
    private String SECRET_KEY;
    private SecretKey key;

    public String generateToken(String user) {
        Date expiration = Date.from(ZonedDateTime.now().plusDays(1).toInstant());
        return Jwts.builder()
                .claim("sub", user)
                .issuedAt(Date.from(Instant.now()))
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build().isSigned(token);
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
