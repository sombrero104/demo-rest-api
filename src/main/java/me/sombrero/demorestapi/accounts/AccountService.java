package me.sombrero.demorestapi.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * 현재 이 예제에서는 email이 계정 아이디 역할을 한다.
         */
        // TODO orElseThrow란?
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)); // 사용자가 없을 경우 에러 메세지 던짐.

        // 이제 Account를 UserDetails 형태로 변환해서 반환해야 하는데
        // 스프링 시큐리티가 UserDetails를 구현한 User를 만들어놓았다.
        // 우리는 이 User를 사용하여 Account 정보를 User에 주어서 반환한다.
        return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
    }

    /**
     * ROLE을 Authority로 변환해주는 메소드를 만들었다.
     */
    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }

}
