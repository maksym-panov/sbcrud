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

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookDao {

    private BookRepository bookRepository;

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

    public Set<Book> getByGenres(Set<Genre> genres) {
        return bookRepository.findBooksByGenres(genres, genres.size());
    }

    public Book updateWithGenres(Book book, Set<Genre> genres) {
        book.setGenres(genres);
        return bookRepository.save(book);
    }

    public void deleteBookById(UUID id) {
        bookRepository.deleteById(id);
    }

}
