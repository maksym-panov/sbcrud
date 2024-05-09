package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Book;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.Favourite;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository.FavouriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class FavouriteDao {

    private FavouriteRepository favouriteRepository;

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
