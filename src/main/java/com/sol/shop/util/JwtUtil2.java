package com.sol.shop.util;

import com.sol.shop.member.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil2 {

    private static final String PRIVATE_KEY = "MIIBVQIBADANBgkqhkiG9w0BAQEFAjA";
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A";

    public static String createToken(Authentication auth) {
        try {
            var user = (CustomUser) auth.getPrincipal();
            var authorities = auth.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.joining(","));

            return Jwts.builder()
                    .claim("username", user.getUsername())
                    .claim("displayName", user.displayName)
                    .claim("authorities", authorities)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 10000)) // 유효 기간 10초
                    .signWith(getPrivateKey())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating JWT token", e);
        }
    }

    private static PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private static PublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public static Claims extractToken(String token) throws Exception {
        PublicKey publicKey = getPublicKey();
        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}