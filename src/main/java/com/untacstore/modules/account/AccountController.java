package com.untacstore.modules.account;

import com.untacstore.modules.account.form.SignUpForm;
import com.untacstore.modules.account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

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

}
