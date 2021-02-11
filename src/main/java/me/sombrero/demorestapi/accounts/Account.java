package me.sombrero.demorestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    /**
     * 여러개의 enum을 가질 수 있으므로 @ElementCollection으로 지정해줘야함.
     * 덧붙여 기본적으로 FetchType이 LAZY 패치인데
     * 이 경우에는 가져올 데이터가(여기에서는 role)이 수가 적은데다가
     * 매번 account 정보를 가져올 때마다 필요한 정보이기 때문에 EAGER 모드로 패치하도록 지정해준다.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles;

}
