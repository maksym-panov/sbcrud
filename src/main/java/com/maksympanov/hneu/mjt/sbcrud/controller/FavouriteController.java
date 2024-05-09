package com.maksympanov.hneu.mjt.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.auth.AuthContextHolder;
import com.maksympanov.hneu.mjt.sbcrud.config.APIPrefixes;
import com.maksympanov.hneu.mjt.sbcrud.dto.AbstractResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.favourite.CreateFavouriteDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.favourite.FavouritesDto;
import com.maksympanov.hneu.mjt.sbcrud.exception.FormValidationException;
import com.maksympanov.hneu.mjt.sbcrud.service.FavouriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(APIPrefixes.USER)
@AllArgsConstructor
public class FavouriteController {

    private FavouriteService favouriteService;

    private DataMapper dataMapper;

    private AuthContextHolder authContextHolder;

    @GetMapping("/favourites")
    public AbstractResponseDto<FavouritesDto> getAllFavouritesOfUser() {
        var authenticatedUser = authContextHolder.getCurrentUser();
        log.info("getAllFavouritesOfUser: for userId: {}", authenticatedUser.getId());
        var favouritesOfUser = favouriteService.getFavouritesOfUser(authenticatedUser);
        var rv = dataMapper.mapFavouritesDto(favouritesOfUser);
        return new AbstractResponseDto<>(rv);
    }

    @PostMapping("/favourites")
    public AbstractResponseDto<FavouritesDto> createNewFavouriteOfUser(
            @Valid @RequestBody CreateFavouriteDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("createNewFavouriteOfUser: found validation error in favourite creation request: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var authenticatedUser = authContextHolder.getCurrentUser();
        var bookId = UUID.fromString(dto.getBookId());
        var newFavourites = favouriteService.createNewFavouriteForUser(authenticatedUser, bookId);
        var rv = dataMapper.mapFavouritesDto(newFavourites);
        return new AbstractResponseDto<>(rv);
    }

    @DeleteMapping("/favourites/{favouriteId}")
    public AbstractResponseDto<FavouritesDto> removeUserFavourite(@PathVariable String favouriteId) {
        var id = UUID.fromString(favouriteId);
        var authenticatedUser = authContextHolder.getCurrentUser();
        log.info("removeUserFavourite: favouriteId - {}, userId - {}", favouriteId, authenticatedUser.getId());
        var newFavourites = favouriteService.removeFavouriteOfUser(authenticatedUser, id);
        var rv = dataMapper.mapFavouritesDto(newFavourites);
        return new AbstractResponseDto<>(rv);
    }

}
