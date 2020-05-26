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
        waiting.setAvailable(false);
        waiting.setAttended(false);
        store.addWaiting(waiting);
        waitingRepository.save(waiting);
    }

    public void exitWaiting(Store store, Waiting waiting) {
        store.getWaitingList().stream().forEach(w-> {
            if (w.getTurn() > waiting.getTurn()) {
                w.setTurn(w.getTurn()-1);
            }
        });
        store.removeWaiting(waiting);
        waitingRepository.delete(waiting);
    }

    public void acceptWaiting(Waiting waiting) {
        if (!waiting.isAvailable() && !waiting.isAttended()) {
            waiting.setAvailable(true);
        }
    }

    public void rejectWaiting(Waiting waiting) {
        if (waiting.isAvailable() && !waiting.isAttended()) {
            waiting.setAvailable(false);
        }
    }

    public void checkIn(Store store, Waiting waiting) {
        if (waiting.isAvailable() && !waiting.isAttended()) {
            waiting.setAttended(true);
            store.shiftTurn(waiting);
        }
    }

    public void cancelCheck(Store store, Waiting waiting) {
        if (waiting.isAvailable() && waiting.isAttended()) {
            waiting.setAttended(false);
            waiting.setTurn(store.countWaitingList()+1);
        }
    }
}
