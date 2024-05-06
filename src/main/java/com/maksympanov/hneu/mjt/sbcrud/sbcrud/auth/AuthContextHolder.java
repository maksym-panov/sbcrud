package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.NonAuthenticatedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthContextHolder {

    public static CustomAuthenticationToken getAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof CustomAuthenticationToken) {
            return (CustomAuthenticationToken) auth;
        }

        throw new NonAuthenticatedException("User is not authenticated");
    }
}
