package com.untacstore.modules.store.controller;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.authentication.CurrentAccount;
import com.untacstore.modules.account.service.AccountService;
import com.untacstore.modules.store.Store;
import com.untacstore.modules.store.form.StoreProfileForm;
import com.untacstore.modules.store.repository.StoreRepository;
import com.untacstore.modules.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Transactional
@RequiredArgsConstructor
@RequestMapping("/store/{path}/settings")
public class StoreSettingsController {
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final ModelMapper modelMapper;

    /*상점 프로필 - 폼*/
    @GetMapping("/profile")
    public String storeProfileForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        model.addAttribute(account);

        Store store = storeRepository.findStoreByPath(path);
        model.addAttribute(store);
        StoreProfileForm storeProfileForm = modelMapper.map(store, StoreProfileForm.class);
        model.addAttribute("storeProfileForm", storeProfileForm);

        return "store/settings/profile";
    }
    /*상점 프로필 - 수정*/
    @PostMapping("/profile")
    public String updateStoreProfile(@CurrentAccount Account account, @PathVariable String path, @Valid StoreProfileForm storeProfileForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "store/settings/profile";
        }

        Store store = storeRepository.findStoreByPath(path);
        storeService.updateProfile(store, storeProfileForm);

        return "redirect:/store/" + URLEncoder.encode(path, StandardCharsets.UTF_8) + "/settings/profile";
    }

    //TODO 메뉴 설정
    //TODO 테이블 설정
    //TODO 키워드 / 주소 설정
    //TODO
    //TODO 상점 설정
}
