package me.sombrero.demorestapi.configs;

import me.sombrero.demorestapi.accounts.AccountService;
import me.sombrero.demorestapi.common.BaseControllerTest;
import me.sombrero.demorestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() {
        this.mockMvc.perform(post("/oauth/token"))
    }

}