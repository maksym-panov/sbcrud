package com.maksympanov.hneu.mjt.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {

    private UserRole role;

    @Override
    public String getAuthority() {
        return role.getRoleName();
    }
}
