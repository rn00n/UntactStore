package com.untacstore.modules.account;

import com.untacstore.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @GetMapping("/sign-up")
    public String signUpForm(Model model, @RequestParam String type) {
        model.addAttribute("signUpForm", new SignUpForm());
        model.addAttribute("type", type);
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(SignUpForm signUpForm) {
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        System.out.println("성공");
        return "redirect:/";
    }

}
