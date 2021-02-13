package me.sombrero.demorestapi.configs;

import me.sombrero.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * [스프링 시큐리티 OAuth2.0]
 * [스프링 시큐리티 공통 설정]
 * Authorization Server와 Resource Server가 공통으로 사용할 설정을 추가해야 한다.
 * 아래와 같이 @EnableWebSecurity를 붙이고 WebSecurityConfigurerAdapter를 상속받는다.
 * 이 두 설정을 추가하는 순간부터는 스프링부트가 기본적으로 제공하는 스프링 시큐리티 설정은 더이상 적용되지 않는다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * UserDetailsService를 상속받는 AccountService를 가져온다.
     */
    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * TokenStore: OAuth 토큰을 저장하는 저장소.
     */
    @Bean
    public TokenStore tokenStore() {
        // return new JdbcTokenStore();
        return new InMemoryTokenStore(); // 저장소는 InMemoryTokenStore로 사용.
    }

    /**
     * 다른 AuthorizationServer나 ResourceServer가 참조할 수 있도록
     * AuthenticationManager를 빈으로 만들어준다.
     * authenticationManagerBean()를 오버라이딩해서 @Bean을 붙여주면 빈으로 된다.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManager를 어떻게 만들 것인지 정하기 위해
     * AuthenticationManagerBuilder를 재정의한다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 1. 웹의 필터로 거르는 방법.
     * SecurityFilter를 적용할지 말지를 WebSecurity를 통해 결정한다.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 필터를 사용하지 않을 경로를 설정해준다. 이외에는 모두 최초 접근 시 로그인 페이지가 뜬다.
        // 원래 스프링 시큐리티는 모든 요청은 인증에 걸린다.
        web.ignoring().mvcMatchers("/docs/index.html"); // 필터를 사용하지 않을 경로.
        web.ignoring().mvcMatchers("/index.html"); // 필터를 사용하지 않을 경로.

        // 스프링부트가 제공해주는 static 리소스들에 대한 기본 위치들을 다 가져와서 스프링 시큐리티가 적용되지 않도록 한다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 2. 스프링 시큐리티 필터체인으로 거르는 방법.
     * WebSecurity 말고 HttpSecurity로 거르는 방법도 있다.
     * HttpSecurity로 거르는 방법은 스프링 시큐리티 안에 들어와서 거르는 것이다.
     * WebSecurity 말고 HttpSecurity로 사용하면
     * 필터로 거르는 것을 사용하지 않기 때문에 무조건 스프링 시큐리티 안으로 들어오게 되고
     * 그 다음에 스프링 시큐리티 필터체인(11개 정도의 체인)을 타게 된다.
     * 웹에서 거르는 필터보다 좀 더 하는 일이 많고 무겁다.
     * 때문에 사실상 static 리소스들은 전부 허용할 것이라면
     * 웹의 필터 자체에서 걸러주는 것이 서버가 조금이라도 일을 덜하게 되므로 부담을 덜어준다.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
            .mvcMatchers("/docs/index.html").anonymous()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
        ;*/

        http
            .anonymous() // 익명 사용자 허용.
                .and()
            .formLogin() // 폼 인증 사용. (경로를 지정하지 않으면 스프링 시큐리티 기본 페이지 사용.)
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll() // /api로 시작하는 모든 GET 요청의 접근을 허용.
                // .mvcMatchers(HttpMethod.GET, "/api/**").authenticated() // /api로 시작하는 모든 GET 요청은 인증(로그인) 필요.
                .anyRequest().authenticated() // 나머지는 인증을 필요로 하겠다.
        ;
    }

}
