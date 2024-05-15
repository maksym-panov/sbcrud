package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GenreDao {

    private GenreRepository genreRepository;

    public Genre createGenre(String genreName, String description) {
        return genreRepository.save(
                Genre.builder()
                        .genreName(genreName)
                        .description(description)
                        .build()
        );
    }

    public Genre getGenreByIdThrowable(UUID id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find Genre with id: " + id));
    }

    public Genre getGenreByIdNullable(UUID id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Page<Genre> getGenresPageable(int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("genreName"));
        return genreRepository.findAll(request);
    }

    public Page<Genre> getGenresByPartialName(String partialName, int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("genreName"));
        return genreRepository.findGenresByGenreNameContaining(partialName, request);
    }

    public Genre updateFullData(Genre genre) {
        return genreRepository.save(genre);
    }

}
