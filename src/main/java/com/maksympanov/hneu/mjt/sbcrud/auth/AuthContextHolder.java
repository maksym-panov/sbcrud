package com.maksympanov.hneu.mjt.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.exception.AuthException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthContextHolder {

    public ServiceUser getCurrentUser() {
        return getAuthentication().getSubject().getUser();
    }

    public static CustomAuthenticationToken getAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof CustomAuthenticationToken) {
            return (CustomAuthenticationToken) auth;
        }

        throw new AuthException("User is not authenticated");
    }
}
