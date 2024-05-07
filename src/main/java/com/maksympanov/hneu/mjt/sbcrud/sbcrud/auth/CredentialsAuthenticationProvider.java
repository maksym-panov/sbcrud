package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.AuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CredentialsAuthenticationProvider implements AuthenticationProvider {

    private AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        var username = (String) authentication.getPrincipal();
        var password = (String) authentication.getCredentials();

        log.info("Login attempt, username: {}", username);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new AuthException("Failed login attempt - invalid form data");
        }

        var user = authenticationService.credentialsLogin(username, password);

        return new CustomAuthenticationToken(new AuthenticatedUser(user));
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

}
