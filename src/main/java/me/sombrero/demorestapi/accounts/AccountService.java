package me.sombrero.demorestapi.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserDetailsService를 상속받는 AccountService를 만든다.
 * DB에서 username으로 찾아서 꺼내온 사용자 정보를
 * UserDetails로 반환하는 loadUserByUsername()을 구현한다.
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder; // 패스워드 인코더를 받아서 패스워드를 저장할 때 인코딩해준다.

    /**
     * Account를 저장하는 메소드.
     * 패스워드를 인코딩하여 저장함.
     */
    public Account saveAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    /**
     * DB에서 특정 username에 해당하는 사용자 정보를 가져와서
     * UserDetails로 반환해주는 메소드.
     */
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
        // return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));

        /**
         * 컨트롤러에서 User가 아닌 Account로 현재 사용자 정보를 가져오기 위해
         * AccountAdapter를 만들어서 사용한다.
         * AccountAdapter는 User를 상속받고 User는 UserDetails를 상속받는다.
         * AccountAdapter와 User의 다른 점은 AccountAdapter는 User의 기능도 가지면서
         * Account를 저장하도록 만들었다. 때문에 AccountAdapter 언제든 Account를 꺼내서 쓸 수 있다.
         */
        return new AccountAdapter(account);
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
