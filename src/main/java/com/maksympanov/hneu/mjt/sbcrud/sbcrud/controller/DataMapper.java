package com.maksympanov.hneu.mjt.sbcrud.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BookDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.book.BooksDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.favourite.FavouriteDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.favourite.FavouritesDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre.GenreDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre.GenresDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.genre.ProductGenreDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.user.UserInfoDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.user.UsersInfoDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Favourite;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Genre;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataMapper {
    
    public BooksDto mapBooksDto(List<Book> books) {
        return new BooksDto(
                books.stream()
                        .map(this::mapBookDto)
                        .toList()
        );
    }

    public BookDto mapBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId().toString())
                .isbn(book.getIsbn())
                .bookName(book.getBookName())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .imageUri(book.getImageUri())
                .genres(mapProductGenres(book.getGenres()))
                .build();
    }

    public List<ProductGenreDto> mapProductGenres(List<Genre> genres) {
        return genres.stream()
                .map( g ->
                        ProductGenreDto.builder()
                                .id(g.getId().toString())
                                .genreName(g.getGenreName())
                                .build()
                )
                .toList();
    }

    public FavouritesDto mapFavouritesDto(List<Favourite> favourites) {
        return FavouritesDto.builder()
                .favourites(
                        favourites.stream()
                                .map(this::mapFavouriteDto)
                                .toList()
                )
                .build();
    }

    public FavouriteDto mapFavouriteDto(Favourite favourite) {
        return FavouriteDto.builder()
                .id(favourite.getId().toString())
                .bookId(favourite.getBook().getId().toString())
                .bookName(favourite.getBook().getBookName())
                .build();
    }

    public GenresDto mapGenresDto(List<Genre> genres) {
        return GenresDto.builder()
                .genres(
                        genres.stream()
                                .map(this::mapGenreDto)
                                .toList()
                )
                .build();
    }

    public GenreDto mapGenreDto(Genre genre) {
        return GenreDto.builder()
                .genreId(genre.getId().toString())
                .genreName(genre.getGenreName())
                .description(genre.getDescription())
                .build();
    }

    public UsersInfoDto mapUsersInfoDto(List<ServiceUser> users) {
        return UsersInfoDto.builder()
                .users(
                        users.stream()
                                .map(this::mapUserInfoDto)
                                .toList()
                )
                .build();
    }

    public UserInfoDto mapUserInfoDto(ServiceUser user) {
        return UserInfoDto.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
    
}
