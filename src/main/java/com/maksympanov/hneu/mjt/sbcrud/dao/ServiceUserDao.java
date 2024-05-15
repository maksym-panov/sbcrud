package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import com.maksympanov.hneu.mjt.sbcrud.repository.ServiceUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ServiceUserDao {

    private ServiceUserRepository serviceUserRepository;

    public ServiceUser createNewUser(
            String email,
            String passwordHash,
            String phoneNumber,
            String firstName,
            String lastName,
            UserRole role
    ) {
        log.info(
                "Creating user with email: {}, firstName: {}, lastName: {}, phoneNumber: {}, role: {}",
                email,
                firstName,
                lastName,
                phoneNumber,
                role
        );
        return serviceUserRepository.save(
                ServiceUser.builder()
                        .email(email)
                        .passwordHash(passwordHash)
                        .phoneNumber(phoneNumber)
                        .firstName(firstName)
                        .lastName(lastName)
                        .role(role)
                        .build()
        );
    }

    public ServiceUser getUserByIdThrowable(UUID id) {
        return serviceUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find ServiceUser with id: " + id));
    }

    public ServiceUser getUserByEmailThrowable(String email) {
        return serviceUserRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Could not find ServiceUser with email: " + email));
    }

    public Page<ServiceUser> getUsersPageable(int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("dateCreated"));
        return serviceUserRepository.findAll(request);
    }

    public ServiceUser updateWithRole(ServiceUser user, UserRole newRole) {
        user.setRole(newRole);
        return serviceUserRepository.save(user);
    }

    public ServiceUser updateFullUserInfo(ServiceUser user) {
        return serviceUserRepository.save(user);
    }

    public ServiceUser updateWithPassword(ServiceUser user, String newPasswordHash) {
        if (newPasswordHash != null) {
            user.setPasswordHash(newPasswordHash);
        }
        return serviceUserRepository.save(user);
    }

}
