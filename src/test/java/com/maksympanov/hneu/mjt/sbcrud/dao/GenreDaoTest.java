package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.getRandomAlphabeticalString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GenreDaoTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreDao genreDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create new genre")
    void shouldCreateGenre() {
        // Given
        var genreName = getRandomAlphabeticalString(20);
        var description = getRandomAlphabeticalString(100);
        var expectedGenre = Genre.builder()
                .id(UUID.randomUUID())
                .genreName(genreName)
                .description(description)
                .build();

        when(genreRepository.save(any())).thenReturn(expectedGenre);

        // When
        var createdGenre = genreDao.createGenre(genreName, description);

        // Then
        assertThat(createdGenre).isNotNull();
        assertThat(expectedGenre).isEqualTo(createdGenre);
        verify(genreRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get genre by ID (Throwable)")
    void shouldGetGenreByIdThrowable() {
        // Given
        var id = UUID.randomUUID();
        var expectedGenre = Genre.builder()
                .id(id)
                .genreName(getRandomAlphabeticalString(30))
                .description(getRandomAlphabeticalString(200))
                .build();
        when(genreRepository.findById(id)).thenReturn(Optional.of(expectedGenre));

        // When
        var foundGenre = genreDao.getGenreByIdThrowable(id);

        // Then
        assertThat(foundGenre).isNotNull();
        assertThat(expectedGenre).isEqualTo(foundGenre);
        verify(genreRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception if there is no such genre")
    void shouldThrowExceptionIfNoSuchGenre() {
        // Given
        var notExistingId = UUID.randomUUID();
        var errorMessage = "Could not find Genre with id: %s".formatted(notExistingId);

        // Then
        assertThatThrownBy(() -> genreDao.getGenreByIdThrowable(notExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should get genre by ID (Nullable)")
    void shouldGetGenreByIdNullable() {
        // Given
        var id = UUID.randomUUID();
        var expectedGenre = Genre.builder()
                .id(id)
                .genreName(getRandomAlphabeticalString(30))
                .description(getRandomAlphabeticalString(150))
                .build();
        when(genreRepository.findById(id)).thenReturn(Optional.of(expectedGenre));

        // When
        var foundGenre = genreDao.getGenreByIdNullable(id);

        // Then
        assertThat(foundGenre).isNotNull();
        assertThat(expectedGenre).isEqualTo(foundGenre);
        verify(genreRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return NULL if there is no such genre")
    void shouldReturnNullIfNoSuchGenre() {
        // When
        var nullResult = genreDao.getGenreByIdNullable(UUID.randomUUID());

        // Then
        assertThat(nullResult).isNull();
    }

    @Test
    @DisplayName("Should get pageable list of genres")
    void shouldGetGenresPageable() {
        // Given
        var pageNumber = 0;
        var pageSize = 10;
        var genres = new ArrayList<Genre>();
        genres.add(
                Genre.builder()
                        .genreName(getRandomAlphabeticalString(20))
                        .description(getRandomAlphabeticalString(50))
                        .build()
        );
        var expectedPage = new PageImpl<>(genres);

        when(
                genreRepository.findAll(
                        argThat((PageRequest request) ->
                                request.getPageNumber() == pageNumber &&
                                        request.getPageSize() == pageSize
                        )
                )
        ).thenReturn(expectedPage);

        // When
        var resultPage = genreDao.getGenresPageable(pageNumber, pageSize);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(expectedPage).isEqualTo(resultPage);

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("genre_name"));
        verify(genreRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Should get pageable list of genres by partial name")
    void shouldGetGenresByPartialName() {
        // Given
        var partialName = "Fant";
        var pageNumber = 0;
        var pageSize = 10;
        var genres = new ArrayList<Genre>();
        genres.add(
                Genre.builder()
                        .genreName(getRandomAlphabeticalString(50))
                        .description(getRandomAlphabeticalString(500))
                        .build()
        );
        var expectedPage = new PageImpl<>(genres);

        when(
                genreRepository.findGenresByGenreNameContaining(
                        eq(partialName),
                        argThat(request ->
                                request.getPageNumber() == pageNumber &&
                                        request.getPageSize() == pageSize
                        )
                )
        ).thenReturn(expectedPage);

        // When
        var resultPage = genreDao.getGenresByPartialName(partialName, pageNumber, pageSize);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(expectedPage).isEqualTo(resultPage);

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("genre_name"));
        verify(genreRepository, times(1)).findGenresByGenreNameContaining(partialName, pageRequest);
    }

    @Test
    @DisplayName("Should update genre full data")
    void shouldUpdateGenreFullData() {
        // Given
        var genre = Genre.builder()
                .genreName(getRandomAlphabeticalString(10))
                .description(getRandomAlphabeticalString(70))
                .build();
        when(genreRepository.save(any())).thenReturn(genre);

        // When
        var updatedGenre = genreDao.updateFullData(genre);

        // Then
        assertThat(updatedGenre).isNotNull();
        assertThat(genre).isEqualTo(updatedGenre);
        verify(genreRepository, times(1)).save(genre);
    }
}
