package com.example.product.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        // Получаем роли
        Object rolesObj = extractAllClaims(token).get("roles");

        if (rolesObj instanceof String) {
            // Если роли пришли как строка
            String rolesStr = (String) rolesObj;
            return Arrays.asList(rolesStr.split(","));
        } else if (rolesObj instanceof List) {
            // Если роли пришли как список
            return (List<String>) rolesObj;
        }

        log.warn("Unexpected roles format: {}", rolesObj != null ? rolesObj.getClass().getName() : "null");
        return Collections.emptyList();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        // Для старой версии JJWT используем прямое создание ключа из байтов
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            log.debug("Validating token: {}", token);

            // Логирование текущего ключа (частично, без компрометации безопасности)
            String secretKeyPartial = secretKey.substring(0, 4) + "..." + secretKey.substring(secretKey.length() - 4);
            log.debug("Using secret key starting with: {}", secretKeyPartial);

            // Для старой версии JJWT используем прямое создание ключа из байтов
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            log.info("Token validation successful");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT token validation error: {}", e.getMessage(), e);
            return false;
        }
    }
}