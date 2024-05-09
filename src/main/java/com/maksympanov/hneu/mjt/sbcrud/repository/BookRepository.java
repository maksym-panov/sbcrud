package com.maksympanov.hneu.mjt.sbcrud.repository;

import com.maksympanov.hneu.mjt.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query(
            "select distinct b from Book b " +
            "inner join BookGenre bg on b.id=bg.book.id " +
            "inner join Genre g on g.id=bg.genre.id " +
            "where g.genreName in :genres " +
            "group by b.id " +
            "having count(distinct g) = :genresSize"
    )
    List<Book> findBooksByGenres(List<Genre> genres, int genresSize);

    Page<Book> findByBookNameContaining(String partialName, Pageable pageable);

}
