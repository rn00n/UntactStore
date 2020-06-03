package com.untacstore.modules.store.repository;

import com.untacstore.modules.favorites.Favorites;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import com.untacstore.modules.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StoreRepositoryExtension {
    Page<Store> findByKeyword(String keyword, Pageable pageable);

    List<Store> findStoreWithKeywordByOwner(Set<Keyword> keywords);

    List<Store> findStoreByFavoritesList(List<Favorites> favorites);

    List<List<Store>> findStoreWithAddressByOwner(Set<Location> locations);
}
