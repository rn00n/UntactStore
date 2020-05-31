package com.untacstore.modules.favorites;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    void deleteByAccountAndStore(Account account, Store store);

    Favorites findByAccountAndStore(Account account, Store store);

    @EntityGraph(attributePaths = {"store"})
    List<Favorites> findAccountWithStoreByAccount(Account accountLoaded);

    List<Favorites> findByAccount(Account accountLoaded);
}
