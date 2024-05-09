package com.maksympanov.hneu.mjt.sbcrud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book_genre", schema = "sbc_schema")
public class BookGenre {

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

}
