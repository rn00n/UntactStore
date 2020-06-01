package com.untacstore.modules.main;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.favorites.Favorites;
import com.untacstore.modules.favorites.FavoritesRepository;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.keyword.KeywordRepository;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.table.Tables;
import com.untacstore.modules.table.TablesRepository;
import com.untacstore.modules.waiting.Waiting;
import com.untacstore.modules.waiting.WaitingForm;
import com.untacstore.modules.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class MainController {
    private final AccountRepository accountRepository;
    private final StoreRepository storeRepository;
    private final TablesRepository tablesRepository;
    private final WaitingRepository waitingRepository;
    private final KeywordRepository keywordRepository;
    private final FavoritesRepository favoritesRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            Account accountLoaded = accountRepository.findAccountWithKeywordsById(account.getId());
            model.addAttribute(accountLoaded);

//            내가 등록한 키워드 리스트
            List<Store> myKeywordStoreList = storeRepository.findStoreWithKeywordByOwner(accountLoaded.getKeywords());
            model.addAttribute("myKeywordStoreList", myKeywordStoreList);
//            내즐겨찾기 리스트
            List<Favorites> favorites = favoritesRepository.findByAccount(accountLoaded);
            List<Store> myFavoritesList = storeRepository.findStoreByFavoritesList(favorites);
            model.addAttribute("myFavoritesList", myFavoritesList);
//            즐겨찾기순 리스트
            Pageable pageableFavorites = PageRequest.of(0,5, Sort.by("favoritesCount").descending());
            Page<Store> favoritesPage = storeRepository.findAll(pageableFavorites);
            model.addAttribute("favoritesPage", favoritesPage);
//            평점순 리스트
            Pageable pageableGrade = PageRequest.of(0,5, Sort.by("grade").descending());
            Page<Store> gradePage = storeRepository.findAll(pageableGrade);
            model.addAttribute("gradePage", gradePage);
//            대기표
            List<Waiting> waitingList = waitingRepository.findAllByAccountAndAttendedOrderByTurnAscWaitingAtAsc(account, false);
            model.addAttribute("waitingList", waitingList);
//            이용중인 테이블
            List<Tables> tablesList = tablesRepository.findByAccount(accountLoaded);
            model.addAttribute("tablesList", tablesList);
//            내 상점 목록
            List<Store> myStoreList = storeRepository.findFirst5AllByOwner(accountLoaded);
            model.addAttribute("myStoreList", myStoreList);
            return "index-after-login";
        }

        Pageable pageable = PageRequest.of(0,9, Sort.by("grade").descending());
        Page<Store> storePage = storeRepository.findAll(pageable);//TODO
        model.addAttribute("storePage", storePage);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*대기표*/
    @GetMapping("/my-waiting")
    public String waitingView(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);

        List<Waiting> waitingAll = waitingRepository.findAllByAccount(account);
//        List<Waiting> waitingList = waitingRepository.findAllByAccountAndAttended(account, false);

        List<Waiting> waitingList = new ArrayList<>();
        List<Waiting> endList = new ArrayList<>();
        for (Waiting waiting: waitingAll) {
            if (waiting.isAttended()) {
                endList.add(waiting);
            } else {
                waitingList.add(waiting);
            }
        }

        model.addAttribute("waitingList", waitingList);
        model.addAttribute("endList", endList);

        model.addAttribute("waitingForm", new WaitingForm());
        return "waiting/my-waiting";
    }

    /*검색*/
    @GetMapping("/search/store")
    public String searchStore(@CurrentAccount Account account, @RequestParam String keyword, Model model,
                              @PageableDefault(size=9,sort="grade",direction= Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute(account);

        Page<Store> storePage = storeRepository.findByKeyword(keyword, pageable);
        model.addAttribute("storePage", storePage);

        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty",
                pageable.getSort().toString().contains("grade")?"grade":"favoritesCount");
        return "search";
    }
}
