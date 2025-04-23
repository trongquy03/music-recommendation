package vn.trongquy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.trongquy.controller.request.SignInRequest;
import vn.trongquy.controller.response.TokenResponse;
import vn.trongquy.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j(topic = "Authenticaton-Controller")
@Tag(name = "Authenticaton Controller")
public class AuthenticatonController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Access Token",description = "Get access token and refresh token by username and password")
    @PostMapping("/access-token")
    public TokenResponse getAccessToken(@RequestBody SignInRequest signInRequest) {
        log.info("Access token request");
//        return TokenResponse.builder()
//                .accessToken("DUMMY-ACCESS-TOKEN")
//                .refreshToken("DUMMY-REFRESH-TOKEN")
//                .build();
        return authenticationService.getAccessToken(signInRequest);
    }

    @Operation(summary = "Refresh Token",description = "Get new access token by refresh token")
    @PostMapping("/refresh-token")
    public TokenResponse getRefreshToken(@RequestBody String refreshToken) {
        log.info("Access refresh request");
        return TokenResponse.builder()
                .accessToken("DUMMY-NEW-ACCESS-TOKEN")
                .refreshToken("DUMMY-REFRESH-TOKEN")
                .build();
    }
}

