package com.untacstore.modules.waiting;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/store/{path}")
@RequiredArgsConstructor
public class WaitingController {
    private final AccountRepository accountRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final WaitingRepository waitingRepository;
    private final WaitingService waitingService;

    /*user 시점*/
    /*줄서기*/
    @GetMapping("/new-waiting")
    public String newWaiting(@CurrentAccount Account account, @RequestParam String personnel, @PathVariable String path, Model model) {
        Store store = storeRepository.findStoreWithWaitingByPath(path);

        if (store.getWaitingList().stream().map(Waiting::getAccount).filter(a -> a.equals(account)).collect(Collectors.toList()).isEmpty()) {
            waitingService.newWaiting(account, store, personnel);
        }

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8);
    }
    /*줄서기 취소*/
    @GetMapping("/exit-waiting")
    public String exitWaiting(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Store store = storeRepository.findStoreWithWaitingByPath(path);
        Waiting waiting = waitingRepository.findByAccountAndStore(account, store);

        if (!store.getWaitingList().stream().map(Waiting::getAccount).filter(a -> a.equals(account)).collect(Collectors.toList()).isEmpty()) {
            waitingService.exitWaiting(store, waiting);
        }

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    /*admin 시점*/
    //TODO 줄서기 확인
    /*대기표*/
    @GetMapping("/waiting-list")
    public String waitingListView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        model.addAttribute(account);
        Store store = storeRepository.findStoreByPath(path);
        model.addAttribute(store);

        List<Waiting> waitingList = waitingRepository.findAllByAccountAndStore(account, store);
        model.addAttribute("waitingList", waitingList);

        return "waiting/waiting-list";
    }
    //TODO 줄서기 확인취소
    @GetMapping("/accept-waiting")
    public String acceptWaiting(@CurrentAccount Account account, @RequestParam(name="id") Waiting waiting, @PathVariable String path, Model model) {
        waitingService.acceptWaiting(waiting);
        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/waiting-list";
    }
    @GetMapping("/reject-waiting")
    public String rejectWaiting(@CurrentAccount Account account, @RequestParam(name="id") Waiting waiting, @PathVariable String path, Model model) {
        waitingService.rejectWaiting(waiting);
        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/waiting-list";
    }
}
