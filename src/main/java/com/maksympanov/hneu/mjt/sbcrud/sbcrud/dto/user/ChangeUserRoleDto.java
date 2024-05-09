package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.user;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleDto {

    @NotNull
    private UserRole newRole;

}
