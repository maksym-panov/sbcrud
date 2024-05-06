package com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceUserRepository extends JpaRepository<ServiceUser, UUID> {

    Optional<ServiceUser> findByEmail(String email);

}
