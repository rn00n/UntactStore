package com.untacstore.modules.store.repository;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String storename);

    boolean existsByPath(String path);

    @EntityGraph(attributePaths = {"reviews"})
    Store findStoreWithReviewsByPath(String storepath);

    Store findStoreByPath(String path);

    Store findByOwner(Account account);
}
