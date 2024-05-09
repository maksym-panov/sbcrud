package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.user;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserInfoDto {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @AssertTrue
    public boolean hasAtLeastOneValue() {
        return StringUtils.isNotEmpty(email)
                || StringUtils.isNotEmpty(firstName)
                || StringUtils.isNotEmpty(lastName)
                || StringUtils.isNotEmpty(phoneNumber);
    }
}
