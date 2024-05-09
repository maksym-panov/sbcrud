package com.maksympanov.hneu.mjt.sbcrud.dto.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;

    @AssertTrue
    public boolean isPasswordValid() {
        String regex = "^(?=.*\\p{Lu})(?=.*\\p{Ll})(?=.*\\d)(?=.*[!@#$%^&*()_+]).{10,}$";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(newPassword);
        return matcher.matches();
    }
}
