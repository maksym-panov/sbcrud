package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.ServiceUserDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.UserAuthException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private ServiceUserDao serviceUserDao;

    private PasswordEncoder passwordEncoder;

    public ServiceUser credentialsLogin(String username, String password) {
        var user = serviceUserDao.getUserByEmailThrowable(username);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UserAuthException("Failed login attempt, username: " + username);
        }

        return user;
    }

}
