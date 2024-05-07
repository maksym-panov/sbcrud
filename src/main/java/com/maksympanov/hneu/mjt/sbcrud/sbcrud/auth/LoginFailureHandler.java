package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.BackendErrorCode;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtProviderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@AllArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private JwtProviderService jwtProviderService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        jwtProviderService.writeAuthenticationErrorInResponse(response, BackendErrorCode.BAD_CREDENTIALS);
    }

}
