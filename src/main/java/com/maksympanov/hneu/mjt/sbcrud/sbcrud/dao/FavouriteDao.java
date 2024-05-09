package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Favourite;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository.FavouriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FavouriteDao {

    private FavouriteRepository favouriteRepository;

    public Favourite getFavouriteByIdThrowable(UUID id) {
        return favouriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("getFavouriteByIdThrowable: could not find favourite with id: " + id));
    }

    public Favourite createFavourite(ServiceUser user, Book book) {
        return favouriteRepository.save(
                Favourite.builder()
                        .user(user)
                        .book(book)
                        .build()
        );
    }

    public List<Favourite> getFavouritesOfUser(ServiceUser user) {
        return favouriteRepository.findAllByUserId(user.getId());
    }

    public void deleteFavourite(Favourite favourite) {
        favouriteRepository.delete(favourite);
    }

}
