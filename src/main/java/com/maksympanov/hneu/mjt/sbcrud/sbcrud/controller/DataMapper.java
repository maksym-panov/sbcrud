package com.maksympanov.hneu.mjt.sbcrud.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BookDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BooksDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre.ProductGenreDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataMapper {
    
    public BooksDto mapBooksDto(List<Book> books) {
        return new BooksDto(
                books.stream()
                        .map(this::mapBookDto)
                        .toList()
        );
    }

    public BookDto mapBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId().toString())
                .isbn(book.getIsbn())
                .bookName(book.getBookName())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .imageUri(book.getImageUri())
                .genres(mapProductGenres(book.getGenres()))
                .build();
    }

    public List<ProductGenreDto> mapProductGenres(List<Genre> genres) {
        return genres.stream()
                .map( g ->
                        ProductGenreDto.builder()
                                .id(g.getId().toString())
                                .genreName(g.getGenreName())
                                .build()
                )
                .toList();
    }
    
}
