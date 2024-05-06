package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Favourite;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.UserRole;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository.ServiceUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("date_created"));
        return serviceUserRepository.findAll(request);
    }

    public ServiceUser updateWithFavourites(ServiceUser user, Set<Favourite> newFavourites) {
        newFavourites = newFavourites == null ? new HashSet<>() : newFavourites;
        user.setFavourites(newFavourites);
        return serviceUserRepository.save(user);
    }

    public ServiceUser updateWithRole(ServiceUser user, UserRole newRole) {
        user.setRole(newRole);
        return serviceUserRepository.save(user);
    }

    public ServiceUser updateUserInfo(ServiceUser user, String email, String phoneNumber, String firstName, String lastName) {
        if (email != null) {
            user.setEmail(email);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        if (firstName != null) {
            user.setFirstName(firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        return serviceUserRepository.save(user);
    }

    public ServiceUser updateWithPassword(ServiceUser user, String newPasswordHash) {
        if (newPasswordHash != null) {
            user.setPasswordHash(newPasswordHash);
        }
        return serviceUserRepository.save(user);
    }

}
