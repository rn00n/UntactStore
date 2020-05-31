package com.untacstore.modules.store.repository;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryExtension {
    Store findByName(String storename);

    boolean existsByPath(String path);

    @EntityGraph(attributePaths = {"reviews"})
    Store findStoreWithReviewsByPath(String storepath);

    Store findStoreByPath(String path);

    Store findByOwner(Account account);

    @EntityGraph(attributePaths = {"menuList"})
    Store findStoreWithMenusByPath(String path);

    Store findStoreWithKeywordByPath(String path);

    @EntityGraph(attributePaths = {"waitingList"})
    Store findStoreWithWaitingByPath(String path);

    List<Store> findAllByKeywordsContains(Keyword keyword);

//    @EntityGraph(attributePaths = {"keywords"})
    Store findStoreWithKeywordById(Long id);

    List<Store> findAllByOrderByGrade();
//
    List<Store> findAllByOrderByFavoritesCount();

}
