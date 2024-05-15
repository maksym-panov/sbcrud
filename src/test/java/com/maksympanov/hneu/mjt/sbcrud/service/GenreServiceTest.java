package com.maksympanov.hneu.mjt.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.getRandomAlphabeticalString;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("Should return pageable list of genres")
    void shouldReturnGenresPageable() {
        // Given
        genreRepository.saveAll(List.of(
                createTestGenre(getRandomAlphabeticalString(20), getRandomAlphabeticalString(50)),
                createTestGenre(getRandomAlphabeticalString(30), getRandomAlphabeticalString(120))
        ));

        // When
        var result = genreService.getBookGenresPageable(0, 10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should return pageable list of genres by partial name")
    void shouldReturnGenresByPartialNamePageable() {
        // Given
        var name1 = getRandomAlphabeticalString(30);
        var partial = name1.substring(20);
        genreRepository.saveAll(List.of(
                createTestGenre(name1, getRandomAlphabeticalString(120)),
                createTestGenre(getRandomAlphabeticalString(9), getRandomAlphabeticalString(100))
        ));

        // When
        var result = genreService.getBookGenresByPartialNamePageable(partial, 0, 10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGenreName()).isEqualTo(name1);
    }

    @Test
    @DisplayName("Should return genre info by ID")
    void shouldReturnGenreInfoById() {
        // Given
        var testGenre = createTestGenre(getRandomAlphabeticalString(10), getRandomAlphabeticalString(50));
        var savedGenre = genreRepository.save(testGenre);

        // When
        var result = genreService.getGenreInfoById(savedGenre.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(savedGenre.getGenreName()).isEqualTo(testGenre.getGenreName());
        assertThat(savedGenre.getDescription()).isEqualTo(testGenre.getDescription());
        assertThat(result.getGenreName()).isEqualTo(testGenre.getGenreName());
        assertThat(result.getDescription()).isEqualTo(testGenre.getDescription());
    }

    @Test
    @DisplayName("Should create new genre")
    void shouldCreateNewGenre() {
        // When
        var name = getRandomAlphabeticalString(20);
        var description = getRandomAlphabeticalString(70);
        var result = genreService.createNewGenre(name, description);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGenreName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("Should update genre info")
    void shouldUpdateGenreInfo() {
        // Given
        var testGenre = createTestGenre(getRandomAlphabeticalString(20), getRandomAlphabeticalString(100));
        var savedGenre = genreRepository.save(testGenre);

        // When
        var newName = getRandomAlphabeticalString(100);
        var newDescription = getRandomAlphabeticalString(300);
        var result = genreService.updateGenreInfo(savedGenre.getId(), newName, newDescription);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGenreName()).isEqualTo(newName);
        assertThat(result.getDescription()).isEqualTo(newDescription);
    }

    private Genre createTestGenre(String genreName, String description) {
        var genre = new Genre();
        genre.setGenreName(genreName);
        genre.setDescription(description);
        return genre;
    }

}
