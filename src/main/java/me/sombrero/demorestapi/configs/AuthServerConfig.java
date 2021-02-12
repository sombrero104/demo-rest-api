package me.sombrero.demorestapi.configs;

import me.sombrero.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 인증 토큰을 발급하기 위한 인증 서버(AuthServer) 설정.
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager; // 유저 인증 정보를 가지고 있음.

    @Autowired
    AccountService accountService; // UserDetailsService

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // client_secret을 확인할 때 이 passwordEncoder를 사용한다.
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * 이 예제에서는 inmemory를 사용했지만,
         * 원래는 'clients.jdbc()'를 사용해서 DB로 관리하는 것이 이상적이다.
         */
        // clients.jdbc()
        clients.inMemory()
                .withClient("myApp")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write") // 정의하기 나름?
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60) // access_token이 유효한 시간은 몇초인지.. (여기선 10분)
                .refreshTokenValiditySeconds(6 * 10 * 60); // refresh_token이 유효한 시간 (여기선 1시간)
    }

    /**
     * authenticationManager와 TokenStore와 UserDetailsService를 설정할 수 있다.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        /**
         * authenticationManager는 유저 인증 정보를 가지고 있다.
         * 유저를 확인해야 토큰을 발급 받을 수 있기 때문에 authenticationManager를
         * 우리의 유저 인증 정보를 알고 있는 authenticationManager로 설정해준다.
         */
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore);
    }

}
