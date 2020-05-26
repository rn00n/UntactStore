package com.untacstore.modules.account.service;

import com.untacstore.modules.account.Account;
import com.untacstore.modules.account.AccountReport;
import com.untacstore.modules.account.AccountReportRepository;
import com.untacstore.modules.account.authentication.PrincipalAccount;
import com.untacstore.modules.account.form.Notifications;
import com.untacstore.modules.account.form.Profile;
import com.untacstore.modules.account.form.SignUpForm;
import com.untacstore.modules.account.repository.AccountRepository;
import com.untacstore.modules.keyword.Keyword;
import com.untacstore.modules.location.Location;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AccountReportRepository accountReportRepository;

    /*시큐리티 로그인(UserDetails) 설정*/
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new PrincipalAccount(account);
    }
    
    /*회원가입*/
    public Account processNewAccount(SignUpForm signUpForm) {
        Account account = saveNewAccount(signUpForm);
        //TODO sendSignUpConfirmEmail(account);
        return account;
    }

    /*회원가입 - 새로운 계정 저장*/
    public Account saveNewAccount(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        //TODO Email Token 생성
        return accountRepository.save(account);
    }

    /*로그인*/
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new PrincipalAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    /*프로필 보기*/
    public Account getAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        return account;
    }

    /*##################
    * SettingsController
    * #################*/
    /*프로필 - 수정*/
    public void updateProfile(Account account, Profile myPage) {
        modelMapper.map(myPage, account);
        accountRepository.save(account);
    }

    /*패스워드 - 수정*/
    public void updatePassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }
    
    /*키워드 - 계정에 포함된 키워드 가져오기*/
    public List<Keyword> getKeywords(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getKeywords();
    }

    /*키워드 - 계정에 추가*/
    public void addKeyword(Account account, Keyword keyword) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getKeywords().add(keyword));
    }

    /*키워드 - 계정에서 삭제*/
    public void removeKeyword(Account account, Keyword keyword) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getKeywords().remove(keyword));
    }

    /*장소 - 계정에 포함된 장소 가져오기*/
    public List<Location> getLocations(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getLocations();
    }
    
    /*장소 - 계정에 추가*/
    public void addLocation(Account account, Location location) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getLocations().add(location));
    }

    /*장소 - 계정에서 삭제*/
    public void removeLocation(Account account, Location location) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a -> a.getLocations().remove(location));
    }

    /*알람 - 수정*/
    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications, account);
        accountRepository.save(account);
    }

    public void addReport(Account to, Account from) {
        accountReportRepository.save(AccountReport.builder().to(to).from(from).build());
    }

    public void cancelReport(Account to, Account from) {
        AccountReport accountReport = accountReportRepository.findByToAndFrom(to, from);
        accountReportRepository.deleteByToAndFrom(to, from);
        to.getReport().remove(accountReport);
    }
}
