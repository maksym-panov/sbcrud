package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookDao {

    private BookRepository bookRepository;

    public Book createBook(
            String isbn,
            String bookName,
            String description,
            Integer quantity,
            BigDecimal price,
            String imageUri,
            List<Genre> genres
    ) {
        var book = Book.builder()
                .isbn(isbn)
                .bookName(bookName)
                .description(description)
                .quantity(quantity)
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
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("bookName", "price"));
        return bookRepository.findAll(request);
    }

    public Page<Book> getBooksByPartialName(String partialName, int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("bookName"));
        return bookRepository.findByBookNameContaining(partialName, request);
    }

    public Book updateNewInfo(Book book) {
        return bookRepository.save(book);
    }

}
