package com.untacstore.modules.waiting;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    Waiting findByAccountAndStore(Account account, Store store);

    List<Waiting> findAllByAccountAndAttended(Account account, boolean attended);

    List<Waiting> findAllByStoreOrderByTurnAscWaitingAtAsc(Store store);

    List<Waiting> findAllByAccount(Account account);

    List<Waiting> findAllByStoreAndAttendedOrderByTurnAscWaitingAtAsc(Store store, boolean attended);

    List<Waiting> findAllByAccountAndAttendedOrderByTurnAscWaitingAtAsc(Account account, boolean b);

    Waiting findByAccountAndStoreAndAttended(Account account, Store store, boolean attended);
}
