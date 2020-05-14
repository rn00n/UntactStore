package com.untacstore.modules.store.controller;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.service.AccountService;
import com.untacstore.modules.review.Review;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.form.StoreForm;
import com.untacstore.modules.store.service.StoreService;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.store.validator.StoreFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final StoreFormValidator storeFormValidator;
//    private final ReviewRepository reviewRepository;

    @InitBinder("storeForm")
        public void initBinder_storeForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(storeFormValidator);
    }

    /*Store - 폼*/
    @GetMapping("/new-store")
    public String createStoreForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new StoreForm());
        return "store/form";
    }

    /*Store - 생성*/
    @PostMapping("/new-store")
    public String createStore(@CurrentAccount Account account, @Valid StoreForm storeForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "store/form";
        }
        storeService.newStore(account, storeForm);

        return "redirect:/store/"+ URLEncoder.encode(storeForm.getPath(), StandardCharsets.UTF_8);
    }

    /*내 상점*/
    @GetMapping("mystore")
    public String myStorePage(@CurrentAccount Account account) {
        Store store = storeRepository.findByOwner(account);

        return "redirect:/store/"+ URLEncoder.encode(store.getPath(), StandardCharsets.UTF_8);
    }

    /*Store view*/
    @GetMapping("/{path}")
    public String storePageView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        model.addAttribute(account);

        //TODO 가게 정보
        //TODO 가게 메뉴
        Store store = storeRepository.findStoreWithReviewsByPath(path);
        model.addAttribute(store);

        //TODO 리뷰
        List<Review> reviews = store.getReviews();
        model.addAttribute("review", reviews);

        //TODO owner check
        model.addAttribute("isOwner", account.getUsername().equals(store.getOwner().getUsername()));

        return "store/view";
    }


    /*TODO 리뷰게시판*/
}
