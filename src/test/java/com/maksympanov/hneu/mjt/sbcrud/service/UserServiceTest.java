package com.maksympanov.hneu.mjt.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.TestUtils;
import com.maksympanov.hneu.mjt.sbcrud.dao.ServiceUserDao;
import com.maksympanov.hneu.mjt.sbcrud.exception.PasswordMismatchException;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceUserDao serviceUserDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Value("${sbc.default-password}")
    private String defaultPassword;

    @Test
    @DisplayName("Should get service user by ID")
    void shouldGetServiceUserById() {
        // Given
        var newUser = createTestUser();
        var savedUser = serviceUserDao.createNewUser(newUser.getEmail(), newUser.getPasswordHash(),
                newUser.getPhoneNumber(), newUser.getFirstName(), newUser.getLastName(), newUser.getRole());

        // When
        var fetchedUser = userService.getServiceUserById(savedUser.getId());

        // Then
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(fetchedUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(fetchedUser.getFirstName()).isEqualTo(savedUser.getFirstName());
        assertThat(fetchedUser.getLastName()).isEqualTo(savedUser.getLastName());
    }

    @Test
    @DisplayName("Should perform sign up")
    void shouldPerformSignUp() {
        // When
        var email = getRandomAlphabeticalString(10) + "@maksympanov.com";
        var password = getRandomAlphabeticalString(8) + getRandomNumericString(2) + "!";
        var jwtToken = userService.performSignUp(email, password, getRandomNumericString(10), "Maksym", "Panov");

        // Then
        assertThat(jwtToken).isNotEmpty();
    }

    @Test
    @DisplayName("Should patch user info")
    void shouldPatchUserInfo() {
        // Given
        var newUser = createTestUser();
        var savedUser = serviceUserDao.createNewUser(newUser.getEmail(), newUser.getPasswordHash(),
                newUser.getPhoneNumber(), newUser.getFirstName(), newUser.getLastName(), newUser.getRole());

        // When
        var newEmail = getRandomAlphabeticalString(10) + "@maksympanov.com";
        var newPhone = getRandomNumericString(10);
        var newFirstName = getRandomAlphabeticalString(20);
        var newLastName = getRandomAlphabeticalString(20);

        var patchedUser = userService.patchUserInfo(
                savedUser.getId(),
                newEmail,
                newPhone,
                newFirstName,
                newLastName
        );

        // Then
        assertThat(patchedUser).isNotNull();
        assertThat(patchedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(patchedUser.getEmail()).isEqualTo(newEmail);
        assertThat(patchedUser.getPhoneNumber()).isEqualTo(newPhone);
        assertThat(patchedUser.getFirstName()).isEqualTo(newFirstName);
        assertThat(patchedUser.getLastName()).isEqualTo(newLastName);
    }

    @Test
    @DisplayName("Should change user password")
    void shouldChangeUserPassword() {
        // Given
        var email = getRandomAlphabeticalString(10) + "@maksympanov.com";
        var password = getRandomAlphabeticalString(8) + getRandomNumericString(2) + "!";
        var newPassword = getRandomAlphabeticalString(8) + getRandomNumericString(2) + "!";

        var jwtToken = userService.performSignUp(email, password, getRandomNumericString(1), "Maksym", "Panov");
        var authenticatedUser = jwtService.getJwtUserSubject(jwtToken.substring(7));
        var userId = authenticatedUser.getUser().getId();

        // When
        userService.changePassword(userId, password, newPassword);

        // Then 1
        assertThatThrownBy(() -> userService.changePassword(userId, password, newPassword))
                .isOfAnyClassIn(PasswordMismatchException.class)
                        .hasMessage("Could not change password for user %s since incorrect old password provided", userId);

        // Then 2 (new password should work)
        userService.changePassword(userId, newPassword, getRandomAlphabeticalString(30));
    }

    @Test
    @DisplayName("Should reset user password by admin")
    void shouldResetUserPassword() {
        // Given
        var email = getRandomAlphabeticalString(10) + "@maksympanov.com";
        var password = getRandomAlphabeticalString(8) +  getRandomNumericString(2) + "!";
        var newPassword = getRandomAlphabeticalString(8) + getRandomNumericString(2) + "!";

        var jwtToken = userService.performSignUp(email, password, getRandomNumericString(10), "Maksym", "Panov");
        var authenticatedUser = jwtService.getJwtUserSubject(jwtToken.substring(7));
        var userId = authenticatedUser.getUser().getId();

        // When
        userService.resetPassword(userId);

        // Then
        // Old password does not work
        assertThatThrownBy(() -> userService.changePassword(userId, password, newPassword))
                .isOfAnyClassIn(PasswordMismatchException.class)
                .hasMessage("Could not change password for user %s since incorrect old password provided", userId);

        // Using default password
        userService.changePassword(userId, defaultPassword, newPassword);

        // Should be successful with newPassword
        userService.changePassword(userId, newPassword, getRandomAlphabeticalString(20));
    }

    @Test
    @DisplayName("Should change user role")
    void testChangeUserRole() {
        // Given
        var email = getRandomAlphabeticalString(20) + "@maksympanov.com";
        var phoneNumber = getRandomNumericString(10);
        var initialRole = UserRole.USER;
        var newRole = UserRole.ADMIN;

        var jwtToken = userService.performSignUp(email, defaultPassword, phoneNumber, "Maksym", "Panov");
        var authenticatedUser = jwtService.getJwtUserSubject(jwtToken.substring(7));
        var userId = authenticatedUser.getUser().getId();

        // When
        var userBeforeRoleChange = userService.getServiceUserById(userId);
        assertThat(initialRole).isEqualTo(userBeforeRoleChange.getRole());

        var userAfterRoleChange = userService.changeUserRole(userId, newRole);

        // Then
        assertThat(newRole).isEqualTo(userAfterRoleChange.getRole());
    }

    @Test
    @DisplayName("Should get pageable list of users")
    void testGetUsersListPageable() {
        // Given
        var pageNumber = 0;
        var pageSize = 10;

        // When
        var users = userService.getUsersListPageable(pageNumber, pageSize);

        // Then
        assertThat(users).hasSizeGreaterThan(0);
    }

    private ServiceUser createTestUser() {
        return ServiceUser.builder()
                .email(getRandomAlphabeticalString(10) + "maksympanov.com")
                .passwordHash(passwordEncoder.encode(getRandomAlphabeticalString(8) + getRandomNumericString(2) + "!"))
                .phoneNumber(getRandomNumericString(10))
                .firstName(getRandomAlphabeticalString(30))
                .lastName(getRandomAlphabeticalString(30))
                .role(UserRole.USER)
                .build();
    }

}