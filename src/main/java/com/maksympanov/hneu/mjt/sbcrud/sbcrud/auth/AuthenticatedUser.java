package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Getter
public class AuthenticatedUser extends User {

    private final ServiceUser user;

    public AuthenticatedUser(ServiceUser user) {
        super(user.getEmail(), "Password placeholder", Set.of(() -> user.getRole().toString()));
        this.user = user;
    }

    public AuthenticatedUserSerializable toSerializable() {
        return AuthenticatedUserSerializable.builder()
                .userId(user.getId())
                .username(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

}
