package com.maksympanov.hneu.mjt.sbcrud.sbcrud.service;


import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.GenreDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Genre;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {

    private GenreDao genreDao;

    public List<Genre> getBookGenresPageable(int pageNumber, int pageSize) {
        log.info("getBookGenresPageable: pageNumber - {}, pageSize - {}", pageNumber, pageSize);
        return genreDao.getGenresPageable(pageNumber, pageSize)
                .stream()
                .toList();
    }

    public List<Genre> getBookGenresByPartialNamePageable(String partialName, int pageNumber, int pageSize) {
        log.info(
                "getBookGenresByPartialNamePageable: partialName - {}, pageNumber - {}, pageSize - {}",
                partialName,
                pageNumber,
                pageSize
        );
        return genreDao.getGenresByPartialName(partialName, pageNumber, pageSize)
                .stream()
                .toList();
    }

    public Genre getGenreInfoById(UUID id) {
        return genreDao.getGenreByIdThrowable(id);
    }

    public Genre createNewGenre(String genreName, String description) {
        log.info("createNewGenre: genreName - {}, description - {}", genreName, description);
        return genreDao.createGenre(genreName, description);
    }

    public Genre updateGenreInfo(UUID genreId, String newName, String newDescription) {
        var genre = getGenreInfoById(genreId);
        genre.setGenreName(newName);
        genre.setDescription(newDescription);
        return genreDao.updateFullData(genre);
    }

}
