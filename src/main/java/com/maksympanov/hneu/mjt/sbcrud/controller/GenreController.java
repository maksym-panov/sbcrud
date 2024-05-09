package com.maksympanov.hneu.mjt.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.dto.AbstractResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.genre.CreateGenreDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.genre.GenreDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.genre.GenresDto;
import com.maksympanov.hneu.mjt.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.config.APIPrefixes;
import com.maksympanov.hneu.mjt.sbcrud.dto.genre.UpdateGenreInfoDto;
import com.maksympanov.hneu.mjt.sbcrud.exception.FormValidationException;
import com.maksympanov.hneu.mjt.sbcrud.service.GenreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class GenreController {

    private GenreService genreService;

    private DataMapper dataMapper;

    @GetMapping(APIPrefixes.PUBLIC + "/genres")
    public AbstractResponseDto<GenresDto> getGenresPageable(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String partialName

    ) {
        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageSize <= 0) {
            pageNumber = 1;
            pageSize = 20;
        }

        List<Genre> genres;
        if (StringUtils.isNotEmpty(partialName)) {
            genres = genreService.getBookGenresByPartialNamePageable(partialName, pageNumber, pageSize);
        } else {
            genres = genreService.getBookGenresPageable(pageNumber, pageSize);
        }

        var rv = dataMapper.mapGenresDto(genres);

        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.PUBLIC + "/genres/{genreId}")
    public AbstractResponseDto<GenreDto> getGenreInfo(@PathVariable String genreId) {
        var id = UUID.fromString(genreId);
        log.info("getGenreInfo: requested genre id: {}", id);
        var genre = genreService.getGenreInfoById(id);
        var rv = dataMapper.mapGenreDto(genre);
        return new AbstractResponseDto<>(rv);
    }

    @PostMapping(APIPrefixes.VENDOR + "/genres")
    public AbstractResponseDto<GenreDto> createNewGenreByVendor(
            @Valid @RequestBody CreateGenreDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("createNewGenreByVendor: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var newGenre = genreService.createNewGenre(dto.getGenreName(), dto.getDescription());
        var rv = dataMapper.mapGenreDto(newGenre);
        return new AbstractResponseDto<>(rv);
    }

    @PutMapping(APIPrefixes.VENDOR + "/genres/{genreId}")
    public AbstractResponseDto<GenreDto> updateGenreInfoByVendor(
            @PathVariable String genreId,
            @Valid @RequestBody UpdateGenreInfoDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("updateGenreInfoByVendor: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }

        var id = UUID.fromString(genreId);
        var newGenre = genreService.updateGenreInfo(id, dto.getGenreName(), dto.getDescription());

        var rv = dataMapper.mapGenreDto(newGenre);

        return new AbstractResponseDto<>(rv);
    }

}
