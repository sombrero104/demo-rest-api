package me.sombrero.demorestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() {
        // Given
        String username = "sombrero104@email.com";
        String password = "1234";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account); // PasswordEncoder를 사용해서 패스워드를 인코딩하여 저장하게 된다.

        // When
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        // PasswordEncoder로 패스워드를 인코딩하여 DB에서 가져온 패스워드와 일치하는지를 테스트한다.
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    // 예외 타입만 확인하는 방법.
    @Test(expected = UsernameNotFoundException.class) // 예외의 타입 확인.
    public void findByUsernameFail() {
        String username = "random@email.com";
        accountService.loadUserByUsername(username);

        // jUnit 5에서는 위에서 처럼 애노테이션으로 예외 타입을 확인하지 않고,
        // 아래와 같이 assertThrows()를 사용해서 예외 타입을 확인할 수 있다.
        // assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(username));
    }

    // 실제 예외를 가지고 와서 확인하는 방법.
    @Test
    public void findByUsernameFail2() {
        String username = "random@email.com";
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed."); // 명시적으로 테스트를 실패하게 함.
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(username);
        }
    }

    // expectedException 사용하는 방법.
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findByUsernameFail3() {
        // 예상되는 예외를 먼저 적어줌.
        String username = "random@email.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        accountService.loadUserByUsername(username);
    }

}