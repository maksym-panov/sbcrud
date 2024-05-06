package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookDao {

    private BookRepository bookRepository;

    public Book createBook(
            String isbn,
            String bookName,
            String description,
            BigDecimal price,
            String imageUri,
            Set<Genre> genres
    ) {
        var book = Book.builder()
                .isbn(isbn)
                .bookName(bookName)
                .description(description)
                .price(price)
                .imageUri(imageUri)
                .genres(genres)
                .build();

        return bookRepository.save(book);
    }

    public Book getBookByIdThrowable(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find Book with id: " + id));
    }

    public Book getBookByIdNullable(UUID id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Page<Book> getBooksPageable(int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("book_name", "price"));
        return bookRepository.findAll(request);
    }

    public Page<Book> getBooksByPartialName(String partialName, int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("book_name"));
        return bookRepository.findByBookNameContaining(partialName, request);
    }

    public Set<Book> getByGenres(Set<Genre> genres) {
        return bookRepository.findBooksByGenres(genres, genres.size());
    }

    public Book updateWithGenres(Book book, Set<Genre> genres) {
        book.setGenres(genres);
        return bookRepository.save(book);
    }

    public Book updateWithImageUri(Book book, String imageUri) {
        book.setImageUri(imageUri);
        return bookRepository.save(book);
    }

    public Book updateWithQuantity(Book book, int newQuantity) {
        book.setQuantity(newQuantity);
        return bookRepository.save(book);
    }

}
