package com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceUserRepository extends CrudRepository<ServiceUser, UUID> {
}
