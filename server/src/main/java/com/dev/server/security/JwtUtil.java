package com.dev.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${app.config.jwt.accessKey}")
    private String accessKey;

    @Value("${app.config.jwt.refreshKey}")
    private String refreshKey;

    @Value("${app.config.jwt.accessDuration}")
    private Long accessDuration;

    @Value("${app.config.jwt.refreshDuration}")
    private Long refreshDuration;

    private final UserDetailsService userDetailsService;

    public String generateToken(UserDetails userDetails, long duration, String secretKey) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + duration * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessDuration, accessKey);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshDuration, refreshKey);
    }

    public UserDetails parseToken(String token, String secretKey) {
        Claims claims = parseClaims(token, secretKey);

        if (claims == null || !validateClaims(claims)) {
            return null;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(claims.getSubject());

        if (userDetails.getUser() == null) {
            return null;
        }

        return userDetails;
    }

    public UserDetails parseAccessToken(String token) {
        return parseToken(token, accessKey);
    }

    public UserDetails parseRefreshToken(String token) {
        return parseToken(token, refreshKey);
    }

    private Claims parseClaims(String token, String secretKey) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }

    }

    private boolean validateClaims(Claims claims) {
        return claims.getSubject() != null
                && claims.getIssuedAt() != null
                && claims.getExpiration() != null
                && claims.getExpiration().after(new Date());
    }
}
