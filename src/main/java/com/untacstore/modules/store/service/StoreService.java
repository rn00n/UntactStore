package com.untacstore.modules.store.service;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.account.service.AccountService;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.menu.form.MenuForm;
import com.untacstore.modules.menu.form.SetmenuForm;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.menu.repository.SetmenuRepository;
import com.untacstore.modules.review.*;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.form.StoreForm;
import com.untacstore.modules.store.form.StoreProfileForm;
import com.untacstore.modules.store.form.StoreSettingsForm;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.table.Tables;
import com.untacstore.modules.table.TablesForm;
import com.untacstore.modules.table.TablesRepository;
import com.untacstore.modules.waiting.Waiting;
import com.untacstore.modules.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final MenuRepository menuRepository;
    private final SetmenuRepository setmenuRepository;
    private final TablesRepository tablesRepository;
    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final WaitingRepository waitingRepository;

    public void newStore(Account account, StoreForm storeForm) {
        Account updateAccount = accountRepository.findById(account.getId()).orElseThrow();

        Store store = modelMapper.map(storeForm, Store.class);
        store.setOwner(updateAccount);

        accountService.login(updateAccount); //현재 로그인된 정보 업데이트

        Store newStore = storeRepository.save(store);
    }

    /*상점 프로필 - 수정*/
    public void updateProfile(Store store, StoreProfileForm storeProfileForm) {
        modelMapper.map(storeProfileForm, store);
        storeRepository.save(store);
    }

    /*메뉴 - 메뉴 추가*/
    public void addMenu(Store store, MenuForm menuForm) {
        Menu newMenu = modelMapper.map(menuForm, Menu.class);
        store.addMenu(newMenu);

        Menu menu = menuRepository.save(newMenu);
    }
    /*메뉴 - 메뉴 삭제*/
    public void removeMenu(Store store, Menu menu) {
        store.removeMenu(menu);
        menuRepository.delete(menu);
    }

    /*메뉴 - 세트메뉴 추가*/
    public void addSetmenu(Store store, SetmenuForm setmenuForm) {
        List<Menu> newMenus = setmenuForm.getMenuList();
        menuRepository.saveAll(newMenus);

        System.out.println("보자보자");
        setmenuForm.getMenuList().stream().forEach(menu -> {
            System.out.print(menu.getName()+":"+ setmenuForm.getTotalPrice() + "+"+menu.getPrice()+":");
            setmenuForm.setTotalPrice(setmenuForm.getTotalPrice() + menu.getPrice());
            System.out.println(setmenuForm.getTotalPrice());
        });

//        setmenuForm.getMenuList().stream().forEach(m -> );

        Setmenu newSetmenu = modelMapper.map(setmenuForm, Setmenu.class);
        newSetmenu.setStore(store);

//        store.addSetmenu(newSetmenu);

        Setmenu setmenu = setmenuRepository.save(newSetmenu);
    }
    /*메뉴 - 세트메뉴 삭제*/
    public void removeSetmenu(Store store, Setmenu setmenu) {
        setmenu.getMenuList().stream().forEach(menu -> menuRepository.delete(menu)); //TODO 쿼리 개선
        store.removeSetmenu(setmenu);
        setmenuRepository.delete(setmenu);
    }

    /*테이블 - 새 테이블*/
    public void newTable(Account account, Store store, TablesForm tablesForm) {
        Tables tables = Tables.builder()
                .tableNum(store.getTableList().size()+1)
                .tablesPath(store.getTableList().size()+1+"")
                .account(null)
                .amount(0)
                .store(store)
                .personnel(tablesForm.getPersonnel())
                .build();
        store.addTable(tables);
        tablesRepository.save(tables);
    }

    /*테이블 - 테이블 삭제*/
    public void removeTable(Store store) {
        Tables tables = store.removeTable();
        tablesRepository.delete(tables);
    }

    /*테이블 - 테이블 수정*/
    public void updateTable(TablesForm tablesForm) {
        Optional<Tables> byId = tablesRepository.findById(tablesForm.getId());
        byId.ifPresent(t -> t.setPersonnel(tablesForm.getPersonnel()));
    }

    /*키워드 - 상점에 키워드 추가*/
    public void addKeyword(Store store, Keyword keyword) {
        Optional<Store> byId = storeRepository.findById(store.getId());
        byId.ifPresent(a -> a.getKeywords().add(keyword));
    }

    /*키워드 - 상에서 키워드 삭제*/
    public void removeKeyword(Store store, Keyword keyword) {
        Optional<Store> byId = storeRepository.findById(store.getId());
        byId.ifPresent(a -> a.getKeywords().remove(keyword));
    }

    /*사업자등록번호 - 수정*/
    public void updateLicensee(Store store, StoreSettingsForm storeSettingsForm) {
        modelMapper.map(storeSettingsForm, store);
    }

    /*리뷰 등록*/
    public void addReview(Account account, Store store, ReviewForm reviewForm) {
        Review review = modelMapper.map(reviewForm, Review.class);
        review.setAccount(account);
        review.setStore(store);
        review.setReviewAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    /*답글 등록*/
    public void addReply(Account account, Store store, ReplyForm replyForm) {
        Optional<Review> byId = reviewRepository.findById(replyForm.getReview_id());

        Reply reply = modelMapper.map(replyForm, Reply.class);
        reply.setAccount(account);
        reply.setStore(store);
        reply.setReview(byId.orElseThrow());
        reply.setReplyAt(LocalDateTime.now());
        replyRepository.save(reply);
    }

    public void openStore(Store store) {
        store.setOpen(true);
    }

    public void closeStore(Store store) {
        store.setOpen(false);
    }

    public void fullStore(Store store) {
        store.setWaiting(true);
    }

    public void UnfilledStore(Store store) {
        store.setWaiting(false);
    }

    //TODO 리뷰 답글 삭제
}
