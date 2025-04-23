package vn.trongquy.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import vn.trongquy.common.TokenType;
import vn.trongquy.exception.InvalidDataException;
import vn.trongquy.service.JwtService;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.trongquy.common.TokenType.ACCESS_TOKEN;
import static vn.trongquy.common.TokenType.REFRESH_TOKEN;

@Service
@Slf4j(topic = "Jwt-Service")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;


    @Override
    public String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generate access token for user {} with authorities {}", userId, authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role",authorities);
//        claims.put("role", authorities.stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList()));

        return generateToken(claims,username);
    }

    @Override
    public String generateRefreshAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generate access token for user {} with authorities {}", userId, authorities);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role",authorities);
//        claims.put("role", authorities.stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList()));

        return generateRefreshToken(claims,username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        log.info("Extract username from token {} with type {}", token, type);
        return extractClaims(type, token, Claims::getSubject);
    }

    private <T> T extractClaims(TokenType type, String token, Function<Claims, T> claimsExtractor) {
        final Claims claims = extraAllClaim(token, type);
        return claimsExtractor.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        try {
            return Jwts.parser().setSigningKey(accessKey).parseClaimsJws(token).getBody();

        }catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied, error: " + e.getMessage());
        }
    }

    private String generateToken(Map<String, Object> claims, String username) {
        log.info("Generate access token for user {} with name {}", username, claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiryMinutes))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        log.info("Generate access token for user {} with name {}", username, claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN ->
            {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN ->
            {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }
    }
}
