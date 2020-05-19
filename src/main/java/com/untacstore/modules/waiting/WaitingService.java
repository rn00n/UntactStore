package com.untacstore.modules.waiting;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final AccountRepository accountRepository;

    /**/
    public void newWaiting(Account account, Store store, String personnel) {
        Waiting waiting = new Waiting();
        waiting.setAccount(account);
        waiting.setPersonnel(Integer.valueOf(personnel));
        waiting.setTurn(store.getWaitingList().size()+1);
        waiting.setWaitingAt(LocalDateTime.now());
        waiting.setAvailable(true);
        waiting.setAttended(false);
        store.addWaiting(waiting);
        waitingRepository.save(waiting);
    }

    public void exitWaiting(Store store, Waiting waiting) {
        store.removeWaiting(waiting);
        waitingRepository.delete(waiting);
    }

    public void acceptWaiting(Waiting waiting) {
        waiting.setAvailable(false);
    }

    public void rejectWaiting(Waiting waiting) {
        waiting.setAvailable(true);
    }
}
