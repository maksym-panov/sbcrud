package com.maksympanov.hneu.mjt.sbcrud.dto.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @Email
    @NotEmpty
    private String email;

    @Size(min = 4, max = 255)
    @NotEmpty
    private String firstName;

    private String lastName;

    private String phoneNumber;

    @NotEmpty
    private String password;

    @AssertTrue
    public boolean isPasswordValid() {
        String regex = "^(?=.*\\p{Lu})(?=.*\\p{Ll})(?=.*\\d)(?=.*[!@#$%^&*()_+]).{10,}$";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
