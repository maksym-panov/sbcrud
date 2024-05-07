package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthenticatedUser subject;

    public CustomAuthenticationToken(AuthenticatedUser subject) {
        super(subject.getAuthorities());
        this.subject = subject;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return subject.getUsername();
    }

}
