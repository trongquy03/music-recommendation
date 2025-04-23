package vn.trongquy.service;

import vn.trongquy.controller.request.SignInRequest;
import vn.trongquy.controller.response.TokenResponse;

public interface AuthenticationService {

    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String request);
}
