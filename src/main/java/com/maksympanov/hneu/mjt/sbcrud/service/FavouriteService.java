package com.maksympanov.hneu.mjt.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.dao.BookDao;
import com.maksympanov.hneu.mjt.sbcrud.dao.FavouriteDao;
import com.maksympanov.hneu.mjt.sbcrud.model.Favourite;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class FavouriteService {

    private FavouriteDao favouriteDao;

    private BookDao bookDao;

    public List<Favourite> getFavouritesOfUser(ServiceUser targetUser) {
        return favouriteDao.getFavouritesOfUser(targetUser);
    }

    public List<Favourite> createNewFavouriteForUser(ServiceUser targetUser, UUID bookId) {
        var book = bookDao.getBookByIdThrowable(bookId);
        favouriteDao.createFavourite(targetUser, book);
        return getFavouritesOfUser(targetUser);
    }

    public List<Favourite> removeFavouriteOfUser(ServiceUser targetUser, UUID favouriteId) {
        var favourite = favouriteDao.getFavouriteByIdThrowable(favouriteId);
        favouriteDao.deleteFavourite(favourite);
        return getFavouritesOfUser(targetUser);
    }


}
