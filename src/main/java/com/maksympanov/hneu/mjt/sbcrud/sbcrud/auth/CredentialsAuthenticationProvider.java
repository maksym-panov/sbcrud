package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.LoginDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.UserAuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CredentialsAuthenticationProvider implements AuthenticationProvider {

    private AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var username = (String) authentication.getPrincipal();
        var password = (String) authentication.getCredentials();

        log.info("Login attempt, username: {}", username);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new UserAuthException("Failed login attempt - invalid form data");
        }

        var user = authenticationService.credentialsLogin(username, password);

        return new CustomAuthenticationToken(new AuthenticatedUser(user));
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

}
