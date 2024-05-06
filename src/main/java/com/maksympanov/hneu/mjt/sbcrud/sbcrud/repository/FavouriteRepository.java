package com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, UUID> {

    Set<Favourite> findAllByUserId(UUID userId);

}
