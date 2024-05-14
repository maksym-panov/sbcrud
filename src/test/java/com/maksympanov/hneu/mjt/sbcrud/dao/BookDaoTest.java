package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookDaoTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookDao bookDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create new book")
    void shouldCreateBook() {
        // Given
        var id = UUID.randomUUID();
        var isbn = getRandomNumericString(10);
        var bookName = getRandomAlphabeticalString(20);
        var description = getRandomAlphabeticalString(100);
        var quantity = getRandomPositiveNumber();
        var price = BigDecimal.valueOf(getRandomPositiveNumber());
        var imageUri = getRandomAlphabeticalString(20) + ".jpg";
        var genres = new ArrayList<Genre>();
        var expectedBook = new Book(id, isbn, bookName, price, quantity, description, imageUri, genres, 0, LocalDateTime.now(), LocalDateTime.now());

        when(bookRepository.save(any())).thenReturn(expectedBook);

        // When
        var createdBook = bookDao.createBook(isbn, bookName, description, quantity, price, imageUri, genres);
        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));

        // Then
        assertThat(createdBook).isNotNull();
        assertThat(expectedBook).isEqualTo(createdBook);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get book by ID (Throwable)")
    void shouldGetBookByIdThrowable() {
        // Given
        var id = UUID.randomUUID();
        var expectedBook = Book.builder()
                .id(id)
                .build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));

        // When
        var foundBook = bookDao.getBookByIdThrowable(id);

        // Then
        assertThat(foundBook).isNotNull();
        assertThat(expectedBook).isEqualTo(foundBook);
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw correct exception if there is no such book")
    void shouldThrowExceptionIfThereIsNoSuchBook() {
        // Given
        var notExistingId = UUID.randomUUID();
        var errorMessage = "Could not find Book with id: %s".formatted(notExistingId);

        // Then
        assertThatThrownBy(() -> bookDao.getBookByIdThrowable(notExistingId))
                .isOfAnyClassIn(NotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should get book by ID (Nullable)")
    void shouldGetBookByIdNullable() {
        // Given
        var id = UUID.randomUUID();
        var expectedBook = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));

        // When
        Book foundBook = bookDao.getBookByIdNullable(id);

        // Then
        assertThat(foundBook).isNotNull();
        assertThat(expectedBook).isEqualTo(foundBook);
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return NULL if there is no such book")
    void shouldReturnNullIfThereIsNoSuchBook() {
        // When
        var nullResult = bookDao.getBookByIdNullable(UUID.randomUUID());

        // Then
        assertThat(nullResult).isNull();
    }

    @Test
    @DisplayName("Should get pageable list of books")
    void shouldGetBooksPageable() {
        // Given
        var pageNumber = getRandomPositiveNumber();
        var pageSize = getRandomPositiveNumber();
        var books = new ArrayList<Book>();
        books.add(new Book());
        var expectedPage = new PageImpl<>(books);

        when(
                bookRepository.findAll(
                        argThat((PageRequest request) ->
                                request.getPageNumber() == pageNumber &&
                                request.getPageSize() == pageSize
                        )
                )
        ).thenReturn(expectedPage);

        // When
        var resultPage = bookDao.getBooksPageable(pageNumber, pageSize);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(expectedPage).isEqualTo(resultPage);

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("bookName", "price"));
        verify(bookRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Should get pageable list of books by partial name")
    void shouldGetBooksByPartialName() {
        // Given
        var partialName = getRandomAlphabeticalString(20);
        var pageNumber = getRandomPositiveNumber();
        var pageSize = getRandomPositiveNumber();
        var books = new ArrayList<Book>();
        books.add(new Book());
        var expectedPage = new PageImpl<>(books);

        when(
                bookRepository.findByBookNameContaining(
                        eq(partialName),
                        argThat(request ->
                                request.getPageNumber() == pageNumber &&
                                request.getPageSize() == pageSize
                        )
                )
        ).thenReturn(expectedPage);

        // When
        var resultPage = bookDao.getBooksByPartialName(partialName, pageNumber, pageSize);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(expectedPage).isEqualTo(resultPage);

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("bookName"));
        verify(bookRepository, times(1)).findByBookNameContaining(partialName, pageRequest);
    }

    @Test
    @DisplayName("Should update book info")
    void shouldUpdateBookInfo() {
        // Given
        var id = UUID.randomUUID();
        var bookToUpdate = new Book();
        bookToUpdate.setId(id);
        when(bookRepository.save(any())).thenReturn(bookToUpdate);

        // When
        var updatedBook = bookDao.updateNewInfo(bookToUpdate);

        // Then
        assertThat(updatedBook).isNotNull();
        assertThat(bookToUpdate).isEqualTo(updatedBook);
        verify(bookRepository, times(1)).save(bookToUpdate);
    }

}
