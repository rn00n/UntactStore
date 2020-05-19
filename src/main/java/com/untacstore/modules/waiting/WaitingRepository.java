package com.untacstore.modules.waiting;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    Waiting findByAccountAndStore(Account account, Store store);

    List<Waiting> findAllByAccount(Account account);

    List<Waiting> findAllByAccountAndStore(Account account, Store store);
}
