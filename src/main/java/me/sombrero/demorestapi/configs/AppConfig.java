package me.sombrero.demorestapi.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

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

}
