package com.untactstore.modules.account.controller;

import com.untactstore.modules.account.Account;
import com.untactstore.modules.account.authentication.CurrentAccount;
import com.untactstore.modules.account.form.SignUpForm;
import com.untactstore.modules.account.repository.AccountRepository;
import com.untactstore.modules.account.service.AccountService;
import com.untactstore.modules.account.validator.SignUpFormValidator;
import com.untactstore.modules.keyword.Keyword;
import com.untactstore.modules.location.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder_signUpForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/email")
    public String emailTest() {
        accountService.sendSignUpConfirmEmail();
        return "redirect:/";
    }
    /*회원가입 - 폼*/
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }
    /*회원가입*/
    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors, Model model, HttpServletRequest request) {
        if (errors.hasErrors()) {
            //SignUpFormValidator 에서 검사
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    /*프로필 보기*/
    @GetMapping("/profile/{username}")
    public String viewProfile(@CurrentAccount Account account, @PathVariable String username, Model model) {
        model.addAttribute(account);
        Account accountToView = accountService.getAccount(username);
        model.addAttribute("profileAccount", accountToView);
        model.addAttribute("isOwner", accountToView.equals(account));

        Set<Keyword> keywords = accountService.getKeywords(account);
        model.addAttribute("keywords", keywords.stream().map(Keyword::getName).collect(Collectors.toList()));
        //로그인된 계정의 location
        Set<Location> locations = accountService.getLocations(account);
        model.addAttribute("locations", locations.stream().map(Location::getName).collect(Collectors.toList()));

        model.addAttribute("profileUsername", username);
        return "account/profile";
    }

    /*계정 - 신고*/
    @GetMapping("/profile/{username}/report")
    public String addReport(@CurrentAccount Account account, @PathVariable String username, Model model) {
        Account to = accountRepository.findByUsername(username);
        accountService.addReport(to, account);

        return "redirect:/profile/"+username;
    }
    /*계정 - 신고취소*/
    @GetMapping("/profile/{username}/report-cancel")
    public String cancelReport(@CurrentAccount Account account, @PathVariable String username, Model model) {
        Account to = accountRepository.findByUsername(username);
        accountService.cancelReport(to, account);

        return "redirect:/profile/"+username;
    }

}
