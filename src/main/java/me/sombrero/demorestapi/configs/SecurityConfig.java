package me.sombrero.demorestapi.configs;

import me.sombrero.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 스프링 시큐리티 OAuth 2.0 적용을 위한 설정 파일.
 * AuthorizationServer와 ResourceServer가 공통으로 사용할 설정을 추가해야 한다.
 * 아래와 같이 @EnableWebSecurity를 붙이고 WebSecurityConfigurerAdapter를 상속받는다.
 * 이 두 설정을 추가하는 순간부터는 스프링부트가 기본적으로 제공하는 스프링 시큐리티 설정은 더이상 적용되지 않는다.
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * UserDetailsService를 상속받는 AccountService를 가져온다.
     */
    @Autowired
    AccountService accountService;



}
