package com.maksympanov.hneu.mjt.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserSerializable implements Serializable {

    private UUID userId;

    private String username;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private UserRole role;

    public AuthenticatedUser toAuthenticatedUser() {
        return new AuthenticatedUser(
                ServiceUser.builder()
                        .id(userId)
                        .email(username)
                        .phoneNumber(phoneNumber)
                        .firstName(firstName)
                        .lastName(lastName)
                        .role(role)
                        .build()
        );
    }

}
