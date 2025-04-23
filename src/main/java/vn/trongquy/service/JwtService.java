package vn.trongquy.service;

import org.springframework.security.core.GrantedAuthority;
import vn.trongquy.common.TokenType;

import java.util.Collection;

public interface JwtService {
    String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String generateRefreshAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String extractUsername(String token, TokenType type);

}
