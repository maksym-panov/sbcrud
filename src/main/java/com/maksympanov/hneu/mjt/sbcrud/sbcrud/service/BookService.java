package com.maksympanov.hneu.mjt.sbcrud.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.BookDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.GenreDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    
    private BookDao bookDao;

    private GenreDao genreDao;
    
    public List<Book> getBooksPageable(int pageNumber, int pageSize) {
        log.info("getBooksPageable: request for pageNumber: {}, pageSize: {}", pageNumber, pageSize);   
        var booksPage = bookDao.getBooksPageable(pageNumber, pageSize);
        return booksPage.stream().toList();
    }

    public List<Book> getBooksByPartialNamePageable(String partialName, Integer pageNumber, Integer pageSize) {
        log.info("getBooksByPartialNamePageable: request for partialName: {}, pageNumber: {}, pageSize: {}", partialName, pageNumber, pageSize);
        var booksPage = bookDao.getBooksByPartialName(partialName, pageNumber, pageSize);
        return booksPage.stream().toList();
    }

    public Book getBookById(UUID id) {
        log.info("getBookById: request for book with id: {}", id);
        return bookDao.getBookByIdThrowable(id);
    }

    public Book createNewBook(
            String isbn,
            String bookName,
            BigDecimal price,
            Integer quantity,
            String description,
            String imageUri,
            List<String> genresIds
    ) {
        if (genresIds == null) {
            genresIds = new ArrayList<>();
        }

        var genres = genresIds.stream()
                .map(UUID::fromString)
                .map(genreDao::getGenreByIdNullable)
                .filter(Objects::nonNull)
                .toList();

        if (quantity == null || quantity < 0) {
            quantity = 0;
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            price = BigDecimal.ZERO;
        }

        return bookDao.createBook(
                isbn,
                bookName,
                description,
                quantity,
                price,
                imageUri,
                genres
        );
    }

    public Book patchBookInfo(
            UUID id,
            String isbn,
            String bookName,
            BigDecimal price,
            Integer quantity,
            String description,
            String imageUri,
            List<String> genresIds
    ) {
        var book = bookDao.getBookByIdThrowable(id);

        if (StringUtils.isNotEmpty(isbn)) {
            book.setIsbn(isbn);
        }

        if (StringUtils.isNotEmpty(bookName)) {
            book.setBookName(bookName);
        }

        if (price != null && price.compareTo(BigDecimal.ZERO) >= 0) {
            book.setPrice(price);
        }

        if (quantity != null && quantity >= 0) {
            book.setQuantity(quantity);
        }

        if (StringUtils.isNotEmpty(description)) {
            book.setDescription(description);
        }

        if (StringUtils.isNotEmpty(imageUri)) {
            book.setImageUri(imageUri);
        }

        if (genresIds != null) {
            var genres = genresIds.stream()
                    .map(UUID::fromString)
                    .map(genreDao::getGenreByIdNullable)
                    .filter(Objects::nonNull)
                    .toList();
            book.setGenres(genres);
        }

        return bookDao.updateNewInfo(book);

    }

}
