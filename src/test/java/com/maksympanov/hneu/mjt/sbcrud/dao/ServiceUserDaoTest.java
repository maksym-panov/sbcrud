package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import com.maksympanov.hneu.mjt.sbcrud.repository.ServiceUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServiceUserDaoTest {

    @Mock
    private ServiceUserRepository serviceUserRepository;

    @InjectMocks
    private ServiceUserDao serviceUserDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create new user")
    void shouldCreateNewUser() {
        // Given
        var email = getRandomAlphabeticalString(10) + "@example.com";
        var passwordHash = getRandomAlphabeticalString(10);
        var phoneNumber = getRandomNumericString(10);
        var firstName = getRandomAlphabeticalString(8);
        var lastName = getRandomAlphabeticalString(8);
        var role = UserRole.USER;
        var expectedUser = new ServiceUser(
                UUID.randomUUID(),
                email,
                getRandomNumericString(10),
                getRandomAlphabeticalString(20),
                getRandomAlphabeticalString(20),
                role,
                passwordHash,
                List.of(),
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(serviceUserRepository.save(any())).thenReturn(expectedUser);

        // When
        var createdUser = serviceUserDao.createNewUser(email, passwordHash, phoneNumber, firstName, lastName, role);

        // Then
        assertThat(createdUser).isNotNull();
        assertThat(expectedUser).isEqualTo(createdUser);
        verify(serviceUserRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get user by ID (Throwable)")
    void shouldGetUserByIdThrowable() {
        // Given
        var id = UUID.randomUUID();
        var expectedUser = new ServiceUser();
        when(serviceUserRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // When
        var foundUser = serviceUserDao.getUserByIdThrowable(id);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(expectedUser).isEqualTo(foundUser);
        verify(serviceUserRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception if there is no such user by ID")
    void shouldThrowExceptionIfNoSuchUserById() {
        // Given
        var notExistingId = UUID.randomUUID();
        var errorMessage = "Could not find ServiceUser with id: %s".formatted(notExistingId);

        // Then
        assertThatThrownBy(() -> serviceUserDao.getUserByIdThrowable(notExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should get user by email (Throwable)")
    void shouldGetUserByEmailThrowable() {
        // Given
        var email = getRandomAlphabeticalString(10) + "@example.com";
        var expectedUser = new ServiceUser();
        when(serviceUserRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // When
        var foundUser = serviceUserDao.getUserByEmailThrowable(email);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(expectedUser).isEqualTo(foundUser);
        verify(serviceUserRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw exception if there is no such user by email")
    void shouldThrowExceptionIfNoSuchUserByEmail() {
        // Given
        var notExistingEmail = getRandomAlphabeticalString(10) + "@example.com";
        var errorMessage = "Could not find ServiceUser with email: %s".formatted(notExistingEmail);

        // Then
        assertThatThrownBy(() -> serviceUserDao.getUserByEmailThrowable(notExistingEmail))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should get pageable list of users")
    void shouldGetUsersPageable() {
        // Given
        var pageNumber = getRandomPositiveNumber();
        var pageSize = getRandomPositiveNumber();
        var users = new ArrayList<ServiceUser>();
        users.add(new ServiceUser());
        var expectedPage = new PageImpl<>(users);

        when(
                serviceUserRepository.findAll(
                        argThat((PageRequest request) ->
                                request.getPageNumber() == pageNumber &&
                                        request.getPageSize() == pageSize
                        )
                )
        ).thenReturn(expectedPage);

        // When
        var resultPage = serviceUserDao.getUsersPageable(pageNumber, pageSize);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(expectedPage).isEqualTo(resultPage);

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("dateCreated"));
        verify(serviceUserRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Should update user with role")
    void shouldUpdateUserWithRole() {
        // Given
        var user = new ServiceUser();
        var newRole = UserRole.ADMIN;
        when(serviceUserRepository.save(any())).thenReturn(user);

        // When
        var updatedUser = serviceUserDao.updateWithRole(user, newRole);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getRole()).isEqualTo(newRole);
        verify(serviceUserRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update user full user info")
    void shouldUpdateUserFullUserInfo() {
        // Given
        var user = new ServiceUser();
        when(serviceUserRepository.save(any())).thenReturn(user);

        // When
        var updatedUser = serviceUserDao.updateFullUserInfo(user);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).isEqualTo(user);
        verify(serviceUserRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update user with password")
    void shouldUpdateUserWithPassword() {
        // Given
        var user = new ServiceUser();
        var newPasswordHash = getRandomAlphabeticalString(10);
        when(serviceUserRepository.save(any())).thenReturn(user);

        // When
        var updatedUser = serviceUserDao.updateWithPassword(user, newPasswordHash);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getPasswordHash()).isEqualTo(newPasswordHash);
        verify(serviceUserRepository, times(1)).save(user);
    }

}