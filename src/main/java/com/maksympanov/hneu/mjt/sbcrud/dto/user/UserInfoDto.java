package com.maksympanov.hneu.mjt.sbcrud.dto.user;

import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String id;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private UserRole role;

}
