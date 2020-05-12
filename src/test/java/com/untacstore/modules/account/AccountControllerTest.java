package com.untacstore.modules.account;

import com.untacstore.modules.account.form.SignUpForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("회원가입 - 폼")
    @Test
    void signUpForm() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setAccountType(AccountType.ADMIN);
        mockMvc.perform(get("/sign-up/form")
                    .sessionAttr("signUpForm", signUpForm))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-form"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated())
        ;
    }

    @DisplayName("회원가입 - 입력값 오류")
    @Test
    void signUpSubmit_wrong_input() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setAccountType(AccountType.ADMIN);

        mockMvc.perform(post("/sign-up/form")
                    .sessionAttr("signUpForm", signUpForm)
                    .param("username", "byungryang")
                    .param("password", "123456") //최소 길이 오류
                    .param("email", "rn00n...") //형식 오류
                    .param("name", "문병량")
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-form"))
                .andExpect(unauthenticated())
        ;
    }

    @DisplayName("회원가입 - 입력값 정상")
    @Test
    void signUpSubmit_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up/form")
                    .param("username", "byungryang")
                    .param("password", "asdfasdf")
                    .param("email", "rn00n@naver.com")
                    .param("name", "문병량")
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("byungryang"))
        ;
    }

}