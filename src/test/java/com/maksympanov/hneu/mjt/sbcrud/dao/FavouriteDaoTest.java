package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.model.Favourite;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.repository.FavouriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class FavouriteDaoTest {

    @Mock
    private FavouriteRepository favouriteRepository;

    @InjectMocks
    private FavouriteDao favouriteDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should get favourite by ID (Throwable)")
    void shouldGetFavouriteByIdThrowable() {
        // Given
        var id = UUID.randomUUID();
        var expectedFavourite = new Favourite();
        when(favouriteRepository.findById(id)).thenReturn(Optional.of(expectedFavourite));

        // When
        var foundFavourite = favouriteDao.getFavouriteByIdThrowable(id);

        // Then
        assertThat(foundFavourite).isNotNull();
        assertThat(expectedFavourite).isEqualTo(foundFavourite);
        verify(favouriteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception if there is no such favourite")
    void shouldThrowExceptionIfNoSuchFavourite() {
        // Given
        var notExistingId = UUID.randomUUID();
        var errorMessage = "getFavouriteByIdThrowable: could not find favourite with id: %s".formatted(notExistingId);

        // Then
        assertThatThrownBy(() -> favouriteDao.getFavouriteByIdThrowable(notExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should create favourite")
    void shouldCreateFavourite() {
        // Given
        var user = new ServiceUser();
        var book = new Book();
        var expectedFavourite = new Favourite();
        when(favouriteRepository.save(any())).thenReturn(expectedFavourite);

        // When
        var createdFavourite = favouriteDao.createFavourite(user, book);

        // Then
        assertThat(createdFavourite).isNotNull();
        assertThat(expectedFavourite).isEqualTo(createdFavourite);
        verify(favouriteRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get favourites of user")
    void shouldGetFavouritesOfUser() {
        // Given
        var user = new ServiceUser();
        var favourites = List.of(new Favourite());
        when(favouriteRepository.findAllByUserId(user.getId())).thenReturn(favourites);

        // When
        var resultFavourites = favouriteDao.getFavouritesOfUser(user);

        // Then
        assertThat(resultFavourites).isNotNull();
        assertThat(resultFavourites).isEqualTo(favourites);
        verify(favouriteRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    @DisplayName("Should delete favourite")
    void shouldDeleteFavourite() {
        // Given
        var favouriteToDelete = new Favourite();

        // When
        favouriteDao.deleteFavourite(favouriteToDelete);

        // Then
        verify(favouriteRepository, times(1)).delete(favouriteToDelete);
    }
}
