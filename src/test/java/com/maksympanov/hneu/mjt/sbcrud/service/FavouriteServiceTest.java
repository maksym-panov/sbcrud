package com.maksympanov.hneu.mjt.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.model.UserRole;
import com.maksympanov.hneu.mjt.sbcrud.repository.BookRepository;
import com.maksympanov.hneu.mjt.sbcrud.repository.FavouriteRepository;
import com.maksympanov.hneu.mjt.sbcrud.repository.ServiceUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.getRandomAlphabeticalString;
import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.getRandomPositiveNumber;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FavouriteServiceTest {

    @Autowired
    private FavouriteService underTest;

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private ServiceUserRepository serviceUserRepository;

    @Autowired
    private BookRepository bookRepository;

    private ServiceUser testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
        testBook = createTestBook();
    }

    private ServiceUser createTestUser() {
        var user = new ServiceUser();
        user.setEmail(getRandomAlphabeticalString(30) + "@maksympanov.com");
        user.setFirstName(getRandomAlphabeticalString(20));
        user.setLastName(getRandomAlphabeticalString(20));
        user.setPasswordHash(getRandomAlphabeticalString(100));
        user.setRole(UserRole.USER);
        return serviceUserRepository.save(user);
    }

    private Book createTestBook() {
        var book = new Book();
        book.setIsbn(getRandomAlphabeticalString(10));
        book.setBookName(getRandomAlphabeticalString(30));
        book.setPrice(BigDecimal.valueOf(getRandomPositiveNumber()));
        book.setQuantity(getRandomPositiveNumber());
        return bookRepository.save(book);
    }

    @Test
    @DisplayName("Should get favourites of user")
    void shouldGetFavouritesOfUser() {
        // Given
        var favourites = underTest.createNewFavouriteForUser(testUser, testBook.getId());

        // When
        var favouritesFetch = underTest.getFavouritesOfUser(testUser);

        // Then
        assertThat(favourites).isNotNull();
        assertThat(favourites).hasSize(1);
        assertThat(favourites.get(0).getBook().getId()).isEqualTo(testBook.getId());
        assertThat(favouritesFetch).isNotNull();
        assertThat(favouritesFetch).hasSize(1);
        assertThat(favourites.get(0).getBook().getId()).isEqualTo(testBook.getId());
    }

    @Test
    @DisplayName("Should create new favourite for user")
    void shouldCreateNewFavouriteForUser() {
        // Given
        var favouritesBefore = underTest.getFavouritesOfUser(testUser);

        // When
        underTest.createNewFavouriteForUser(testUser, testBook.getId());
        var favouritesAfter = underTest.getFavouritesOfUser(testUser);

        // Then
        assertThat(favouritesAfter).isNotNull();
        assertThat(favouritesAfter).hasSize(favouritesBefore.size() + 1);
    }

    @Test
    @DisplayName("Should remove favourite of user")
    void shouldRemoveFavouriteOfUser() {
        // Given
        var favourites = underTest.createNewFavouriteForUser(testUser, testBook.getId());
        var favouriteId = favourites.get(0).getId();

        // When
        underTest.removeFavouriteOfUser(testUser, favouriteId);

        // Then
        assertThat(favouriteRepository.findById(favouriteId)).isEmpty();
    }
}