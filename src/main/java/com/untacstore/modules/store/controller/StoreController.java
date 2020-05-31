package com.untacstore.modules.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.menu.Menu;
import com.untacstore.modules.menu.Setmenu;
import com.untacstore.modules.menu.repository.MenuRepository;
import com.untacstore.modules.menu.repository.SetmenuRepository;
import com.untacstore.modules.order.Payment;
import com.untacstore.modules.order.PaymentRepository;
import com.untacstore.modules.order.form.Sales;
import com.untacstore.modules.order.form.Statistics;
import com.untacstore.modules.review.ReplyForm;
import com.untacstore.modules.review.Review;
import com.untacstore.modules.review.ReviewForm;
import com.untacstore.modules.review.ReviewRepository;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.form.RemoteControl;
import com.untacstore.modules.store.form.StoreForm;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.store.service.StoreService;
import com.untacstore.modules.store.validator.StoreFormValidator;
import com.untacstore.modules.table.Tables;
import com.untacstore.modules.table.TablesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/store")@SessionAttributes("remoteControl")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final StoreFormValidator storeFormValidator;
    private final MenuRepository menuRepository;
    private final SetmenuRepository setmenuRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final TablesRepository tablesRepository;
    private final ObjectMapper objectMapper;

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
        Store store = storeRepository.findStoreWithReviewsByPath(path);
        model.addAttribute(store);

        if (account != null) {
            model.addAttribute(account);
            model.addAttribute("isOwner", account.getUsername().equals(store.getOwner().getUsername()));
        } else {
            model.addAttribute("isOwner", false);
        }
        //리뷰
        List<Review> reviews = store.getReviews();
        model.addAttribute("review", reviews);

        return "store/view";
    }

    /*menu 소개*/
    @GetMapping("/{path}/menu")
    public String menuView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }

        Store store = storeRepository.findStoreWithMenusByPath(path);
        model.addAttribute(store);

        List<Setmenu> setmenuList = setmenuRepository.findAllByStore(store);
        model.addAttribute("setmenuList", setmenuList);

        model.addAttribute("menuList", store.getMenuList());
        return "store/menu";
    }

    /*리뷰게시판 - 폼*/
    @GetMapping("/{path}/review")
    public String reviewView(@CurrentAccount Account account, @PathVariable String path, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        Store store = storeRepository.findStoreWithReviewsByPath(path);
        model.addAttribute(store);

        List<Review> review = reviewRepository.findReviewWithReplyByStore(store);
        model.addAttribute("reviews", review);

//        model.addAttribute("reply", review);

        model.addAttribute("reviewForm", new ReviewForm());

        model.addAttribute("replyForm", new ReplyForm());

        return "store/review";
    }

    /*리뷰게시판 - 리뷰 등록*/
    @PostMapping("/{path}/review/add")
    public String addReview(@CurrentAccount Account account, ReviewForm reviewForm, @PathVariable String path, Model model) {
        Store store = storeRepository.findStoreByPath(path);

        storeService.addReview(account, store, reviewForm);

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/review";
    }
    //TODO 댓글 삭제

    /*리뷰게시판 - 답글 등록*/
    @PostMapping("/{path}/reply/add")
    public String addReply(@CurrentAccount Account account, ReplyForm replyForm, @PathVariable String path, Model model) {
        Store store = storeRepository.findStoreByPath(path);

        storeService.addReply(account, store, replyForm);

        return "redirect:/store/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/review";
    }
    //TODO 답글 삭제

    /*통계 - 뷰*/
    @GetMapping("/{path}/management")
    public String managementView(@CurrentAccount Account account, RemoteControl remoteControl, @PathVariable String path, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Store store = storeRepository.findStoreByPath(path);
        model.addAttribute(store);

        List<Payment> paymentList;
        if (remoteControl.getTableNo().equals("all")) {
            paymentList = paymentRepository.findAllByStoreOrderByPaymentAtDesc(store);
        } else {
            Tables tables = tablesRepository.findByStoreAndTablesPath(store, remoteControl.getTableNo());
            paymentList = paymentRepository.findAllByStoreAndTablesOrderByPaymentAtDesc(store, tables);
        }
        model.addAttribute("paymentList", paymentList);

        putStatistics(paymentList, model);

        //TODO 정보를 (월or일/매출) 맵핑
        Map<LocalDate, Integer> salesList = new HashMap<>();
        //년-월-일
        paymentList.stream().forEach(p->{
            if (salesList.containsKey(LocalDate.from(p.getPaymentAt()))) {//있으면
                salesList.put(LocalDate.from(p.getPaymentAt()), salesList.get(LocalDate.from(p.getPaymentAt()))+p.getPay());
            } else {//없으면
                salesList.put(LocalDate.from(p.getPaymentAt()), p.getPay());
            }
        });
        String map = objectMapper.writeValueAsString(salesList);
        System.out.println(map);

        model.addAttribute(remoteControl);
        return "store/management";
    }

    private void putStatistics(List<Payment> paymentList, Model model) {
        List<Setmenu> setmenuList = new ArrayList<>();
        List<Menu> menuList = new ArrayList<>();
        paymentList.stream().forEach(p->{
            p.getOrderList().stream().forEach(o->{
                setmenuList.addAll(o.getSetMenuList());
                menuList.addAll(o.getMenuList());
            });
        });
        Map<Long, Statistics> statistics = new HashMap<>();
        setmenuList.stream().forEach(sm->{
            if (statistics.containsKey(sm.getId())) {
                statistics.get(sm.getId()).addCount();
            } else {
                statistics.put(sm.getId(), Statistics.builder().name(sm.getTitle()).price(sm.getTotalPrice()).count(1).build());
            }
        });

        menuList.stream().forEach(m->{
            if (statistics.containsKey(m.getId())) {
                statistics.get(m.getId()).addCount();
            } else {
                statistics.put(m.getId(), Statistics.builder().name(m.getName()).price(m.getPrice()).count(1).build());
            }
        });

        model.addAttribute("statistics", statistics.values().stream().collect(Collectors.toList()));
    }
}
