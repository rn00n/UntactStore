package com.untacstore.modules.account.controller;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.form.SignUpForm;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.account.service.AccountService;
import com.untacstore.modules.account.validator.SignUpFormValidator;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("signUpForm")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder_signUpForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    /*회원가입 - 계정 타입 폼*/
    @GetMapping("/sign-up/type")
    public String signUpFormAccountType(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up-type";
    }
    /*회원가입 - 계정 타입*/
    @PostMapping("/sign-up/type")
    public String signUpAccountType(SignUpForm signUpForm, Model model) {
        return "redirect:/sign-up/form";
    }
    /*회원가입 - 폼*/
    @GetMapping("/sign-up/form")
    public String signUpForm(@ModelAttribute SignUpForm signUpForm, Model model) {
        model.addAttribute("signUpForm", signUpForm);
        return "account/sign-up-form";
    }
    /*회원가입*/
    @PostMapping("/sign-up/form")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors, Model model, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            //SignUpFormValidator 에서 검사
            return "account/sign-up-form";
        }
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        sessionStatus.setComplete();
        return "redirect:/";
    }
    
    /*프로필 보기*/
    @GetMapping("/profile/{username}")
    public String viewProfile(@CurrentAccount Account account, @PathVariable String username, Model model) {
        Account accountToView = accountService.getAccount(username);
        model.addAttribute(accountToView);
        model.addAttribute("isOwner", accountToView.equals(account));

        List<Keyword> keywords = accountService.getKeywords(account);
        model.addAttribute("keywords", keywords.stream().map(Keyword::getName).collect(Collectors.toList()));
        //로그인된 계정의 location
        List<Location> locations = accountService.getLocations(account);
        model.addAttribute("locations", locations.stream().map(Location::getName).collect(Collectors.toList()));

        model.addAttribute("profileUsername", username);
        return "account/profile";
    }

    /*계정 - 신고*/
    @GetMapping("/profile/{username}/report")
    public String addReport(@CurrentAccount Account account, @PathVariable String username, Model model) {
        accountService.addReport(account);
        //TODO 신고 필드를 엔티티로 변경
        return "redirect:/profile/"+username;
    }

}
