package com.untacstore.modules.favorites;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;

    public void addFavorites(Account account, Store store) {
        Favorites favorites = new Favorites();
        favorites.setAccount(account);

        store.addFavorites(favorites);

        favoritesRepository.save(favorites);
    }

    public void removeFavorites(Account account, Store store) {
        Favorites favorites = favoritesRepository.findByAccountAndStore(account, store);
        store.removeFavorites(favorites);
        favoritesRepository.delete(favorites);
    }
}
