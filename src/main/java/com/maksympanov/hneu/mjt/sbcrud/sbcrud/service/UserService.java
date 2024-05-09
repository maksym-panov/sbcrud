package com.maksympanov.hneu.mjt.sbcrud.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth.AuthenticatedUser;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.ServiceUserDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.PasswordMismatchException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final ServiceUserDao serviceUserDao;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Value("${sbc.default-password}")
    private String defaultPassword;

    public ServiceUser getServiceUserById(UUID userId) {
        return serviceUserDao.getUserByIdThrowable(userId);
    }

    public List<ServiceUser> getUsersListPageable(Integer pageNumber, Integer pageSize) {
        log.info("getUsersListPageable: pageNubmer - {}, pageSize - {}", pageNumber, pageSize);
        return serviceUserDao.getUsersPageable(pageNumber, pageSize)
                .stream()
                .toList();
    }

    public String performSignUp(String email, String password, String phoneNumber, String firstName, String lastName) {
        var emailLower = StringUtils.toRootLowerCase(email);
        var passwordHash = passwordEncoder.encode(password);

        log.info(
                "performSignUp: creating user with email: {}, phoneNumber: {}, firstName: {}, lastName: {}",
                emailLower,
                phoneNumber,
                firstName,
                lastName
        );
        var registeredUser = serviceUserDao.createNewUser(
                emailLower,
                passwordHash,
                phoneNumber,
                firstName,
                lastName,
                UserRole.USER
        );
        log.info("performSignUp: created user with id: {}", registeredUser.getId());

        var jwtToken = "Bearer " + jwtService.getJwtFromSubject(new AuthenticatedUser(registeredUser));
        log.info("performSignUp: created JWT-bearer: {}", jwtToken);

        return jwtToken;
    }

    public ServiceUser patchUserInfo(UUID userId, String email, String phoneNumber, String firstName, String lastName) {
        log.info(
                "patchUserInfo: change user ({}) info request, email - {}, phoneNumber - {}, firstNumber - {}, lastName - {}",
                userId,
                email,
                phoneNumber,
                firstName,
                lastName
        );
        var user = serviceUserDao.getUserByIdThrowable(userId);

        if (StringUtils.isNotEmpty(email)) {
            user.setEmail(email);
        }

        if (StringUtils.isNotEmpty(phoneNumber)) {
            user.setPhoneNumber(phoneNumber);
        }

        if (StringUtils.isNotEmpty(firstName)) {
            user.setFirstName(firstName);
        }

        if (StringUtils.isNotEmpty(lastName)) {
            user.setLastName(lastName);
        }

        return serviceUserDao.updateFullUserInfo(user);
    }

    public ServiceUser changeUserRole(UUID id, UserRole newRole) {
        log.info("changeUserRole: userId - {}, newRole - {}", id, newRole);
        var user = serviceUserDao.getUserByIdThrowable(id);
        return serviceUserDao.updateWithRole(user, newRole);
    }

    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        var user = serviceUserDao.getUserByIdThrowable(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new PasswordMismatchException("Could not change password for user " + userId + " since incorrect old password provided");
        }
        var newPasswordHash = passwordEncoder.encode(newPassword);
        serviceUserDao.updateWithPassword(user, newPasswordHash);
    }

    public void resetPassword(UUID id) {
        log.info("resetPassword: userId - {}", id);
        var user = serviceUserDao.getUserByIdThrowable(id);
        var newPasswordHash = passwordEncoder.encode(defaultPassword);
        serviceUserDao.updateWithPassword(user, newPasswordHash);
    }
}
