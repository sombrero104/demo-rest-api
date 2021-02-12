package me.sombrero.demorestapi.configs;

import me.sombrero.demorestapi.accounts.Account;
import me.sombrero.demorestapi.accounts.AccountRole;
import me.sombrero.demorestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * createDelegatingPasswordEncoder() 사용하여
     * PasswordEncoder를 만들어서 빈으로 만들어준다.
     * createDelegatingPasswordEncoder()의 코드를 보면 알겠지만,
     * createDelegatingPasswordEncoder()는 인코딩된 패스워드 앞에
     * 어떠한 방식으로 인코딩이 된 것인지 prefix를 붙여준다.
     * 그 다음에 그 인코딩 방식에 따라 적절한 PasswordEncoder를 찾아서
     * 패스워드 값이 일치하는지 확인을 한다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * ApplicationRunner로 애플리케이션이 뜰 때 같이 실행되는 코드를 작성한다.
     * 여기에서는 애플리케이션이 뜰 때 샘플 데이터(account 데이터)를 추가하는 것을 작성하였다.
     */
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account account = Account.builder()
                        .email("sombrero104@email.com")
                        .password("1234")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                accountService.saveAccount(account);
            }
        };
    }

}
