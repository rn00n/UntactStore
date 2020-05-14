package com.untacstore.modules.store.service;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.account.service.AccountService;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.form.StoreForm;
import com.untacstore.modules.store.form.StoreProfileForm;
import com.untacstore.modules.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public void newStore(Account account, StoreForm storeForm) {
        Store store = modelMapper.map(storeForm, Store.class);
        Account updateAccount = accountRepository.findById(account.getId()).orElseThrow();
        store.setOwner(updateAccount);
        accountService.login(updateAccount);
        Store newStore = storeRepository.save(store);
    }

    /*상점 프로필 - 수정*/
    public void updateProfile(Store store, StoreProfileForm storeProfileForm) {
        modelMapper.map(storeProfileForm, store);
        storeRepository.save(store);
    }
}
