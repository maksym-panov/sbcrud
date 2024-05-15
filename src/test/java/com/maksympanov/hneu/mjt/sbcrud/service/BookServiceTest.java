package com.maksympanov.hneu.mjt.sbcrud.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("Should return pageable list of books")
    void shouldReturnBooksPageable() {
        // Given
        var pageNumber = getRandomPositiveNumber() % 10 + 1;
        var pageSize = getRandomPositiveNumber() % 10 + 1;

        // When
        var result = bookService.getBooksPageable(pageNumber, pageSize);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should return pageable list of books by partial name")
    void shouldReturnBooksByPartialNamePageable() {
        // Given
        var partialName = getRandomAlphabeticalString(10);
        var pageNumber = getRandomPositiveNumber() % 1000;
        var pageSize = getRandomPositiveNumber() % 1000;

        // When
        var result = bookService.getBooksByPartialNamePageable(partialName, pageNumber, pageSize);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find book by its ID")
    void shouldReturnBookById() {
        // Given
        var isbn = getRandomNumericString(10);
        var bookName = getRandomAlphabeticalString(20);
        var price = BigDecimal.valueOf(getRandomPositiveNumber());
        var quantity = getRandomPositiveNumber();
        var description = getRandomAlphabeticalString(100);
        var imageUri = getRandomAlphabeticalString(20) + ".jpg";
        var genresIds = new ArrayList<String>();
        var createdBook = bookService.createNewBook(isbn, bookName, price, quantity, description, imageUri, genresIds);

        // When
        var result = bookService.getBookById(createdBook.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(createdBook.getId());
        assertThat(result.getIsbn()).isEqualTo(isbn);
        assertThat(result.getBookName()).isEqualTo(bookName);
        assertThat(result.getPrice()).isEqualByComparingTo(price);
        assertThat(result.getQuantity()).isEqualTo(quantity);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getImageUri()).isEqualTo(imageUri);
        assertThat(result.getGenres()).isEmpty();
    }

    @Test
    @DisplayName("Should create new book")
    void shouldCreateNewBook() {
        // Given
        var isbn = getRandomNumericString(10);
        var bookName = getRandomAlphabeticalString(20);
        var price = BigDecimal.valueOf(getRandomPositiveNumber());
        var quantity = getRandomPositiveNumber();
        var description = getRandomAlphabeticalString(100);
        var imageUri = getRandomAlphabeticalString(20) + ".jpg";
        var genresIds = new ArrayList<String>();

        // When
        var createdBook = bookService.createNewBook(isbn, bookName, price, quantity, description, imageUri, genresIds);

        // Then
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getId()).isNotNull();
        assertThat(createdBook.getIsbn()).isEqualTo(isbn);
        assertThat(createdBook.getBookName()).isEqualTo(bookName);
        assertThat(createdBook.getPrice()).isEqualTo(price);
        assertThat(createdBook.getQuantity()).isEqualTo(quantity);
        assertThat(createdBook.getDescription()).isEqualTo(description);
        assertThat(createdBook.getImageUri()).isEqualTo(imageUri);
        assertThat(createdBook.getGenres()).isEmpty();
    }

    @Test
    @DisplayName("Should change info of given book")
    void shouldPatchBookInfo() {
        // Given
        var isbn = getRandomNumericString(10);
        var bookName = getRandomAlphabeticalString(20);
        var price = BigDecimal.valueOf(getRandomPositiveNumber());
        var quantity = getRandomPositiveNumber();
        var description = getRandomAlphabeticalString(100);
        var imageUri = getRandomAlphabeticalString(20) + ".jpg";
        var genresIds = new ArrayList<String>();
        var createdBook = bookService.createNewBook(isbn, bookName, price, quantity, description, imageUri, genresIds);

        var updatedIsbn = getRandomNumericString(10);
        var updatedBookName = getRandomAlphabeticalString(20);
        var updatedPrice = BigDecimal.valueOf(getRandomPositiveNumber());
        var updatedQuantity = getRandomPositiveNumber();
        var updatedDescription = getRandomAlphabeticalString(100);
        var updatedImageUri = getRandomAlphabeticalString(20) + ".jpg";

        // When
        var patchedBook = bookService.patchBookInfo(
                createdBook.getId(),
                updatedIsbn,
                updatedBookName,
                updatedPrice,
                updatedQuantity,
                updatedDescription,
                updatedImageUri,
                new ArrayList<>()
        );

        // Then
        assertThat(patchedBook).isNotNull();
        assertThat(patchedBook.getId()).isEqualTo(createdBook.getId());
        assertThat(patchedBook.getIsbn()).isEqualTo(updatedIsbn);
        assertThat(patchedBook.getBookName()).isEqualTo(updatedBookName);
        assertThat(patchedBook.getPrice()).isEqualTo(updatedPrice);
        assertThat(patchedBook.getQuantity()).isEqualTo(updatedQuantity);
        assertThat(patchedBook.getDescription()).isEqualTo(updatedDescription);
        assertThat(patchedBook.getImageUri()).isEqualTo(updatedImageUri);
        assertThat(patchedBook.getGenres()).isEmpty();
    }

}