package com.untacstore.modules.main;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.keyword.KeywordRepository;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.waiting.Waiting;
import com.untacstore.modules.waiting.WaitingForm;
import com.untacstore.modules.waiting.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class MainController {
    private final AccountRepository accountRepository;
    private final StoreRepository storeRepository;
    private final WaitingRepository waitingRepository;
    private final KeywordRepository keywordRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);

            List<Store> storeList = storeRepository.findAll();//TODO
            model.addAttribute("storeList", storeList);
        }

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

        List<Waiting> waitingList = waitingRepository.findAllByAccount(account);

        model.addAttribute("waitingList", waitingList.stream().filter(w->w.isAvailable()).collect(Collectors.toList()));
        model.addAttribute("endList", waitingList.stream().filter(w-> (!w.isAvailable())).collect(Collectors.toList()));

        model.addAttribute("waitingForm", new WaitingForm());
        return "waiting/my-waiting";
    }

    /*검색*/
    @GetMapping("/search/store")
    public String searchStore(@CurrentAccount Account account, @RequestParam String keyword, Model model) {
        model.addAttribute(account);

        Keyword keyword1 = keywordRepository.findByName(keyword);
//        List<Store> storeList = storeRepository.findAllByKeywordsContains(keyword1);
//        model.addAttribute("storeList", storeList);

        //TODO keyword 검색으로 변경해야함, Paging 처리, QueryDSL
        List<Store> storeList = storeRepository.findAll();
        model.addAttribute("storeList", storeList);

        model.addAttribute("keyword", keyword);

        return "search";
    }
}
