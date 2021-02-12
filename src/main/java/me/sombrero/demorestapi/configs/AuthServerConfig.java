package me.sombrero.demorestapi.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * 인증 토큰을 발급하기 위한 설정.
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

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
                .scopes("read write") // 정의하기 나름?
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60) // access_token이 유효한 시간은 몇초인지.. (여기선 10분)
                .refreshTokenValiditySeconds(6 * 10 * 60); // refresh_token이 유효한 시간 (여기선 1시간)
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
    }

}
