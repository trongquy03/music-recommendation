package vn.trongquy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.trongquy.controller.request.SignInRequest;
import vn.trongquy.controller.response.TokenResponse;
import vn.trongquy.repository.UserRepository;
import vn.trongquy.service.AuthenticationService;
import vn.trongquy.service.JwtService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Authentication-Service")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        log.info("get access token");

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.error("Login fail , message = {}" ,e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        var user = userRepository.findByUsername(request.getUserName());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(),request.getUserName(), user.getAuthorities());
        String accessRefreshToken = jwtService.generateRefreshAccessToken(user.getId(),request.getUserName(), user.getAuthorities());

        return TokenResponse.builder().accessToken(accessToken).refreshToken(accessRefreshToken).build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        return null;
    }
}
