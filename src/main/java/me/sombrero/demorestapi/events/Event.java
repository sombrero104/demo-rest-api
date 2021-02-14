package me.sombrero.demorestapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.sombrero.demorestapi.accounts.Account;
import me.sombrero.demorestapi.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @EqualsAndHashCode(of = "id")
 * equals()와 hashCode()를 구현할 때 기본적으로 모든 필드들을 다 사용한다.
 * 그런데 만약 엔티티간에 (이 Event 객체가 다른 객체간에) 연관관계가 있을 경우에는
 * 엔티티간에 서로 상호 참조하는 관계가 되면
 * eauals()와 hashCode()를 구현한 코드 안에서 스택오버플로우가 발생할 수 있다.
 * 때문에 여기에서는 id 값만 가지고 equals()와 hashCode()를 서로 비교한다.
 * 원한다면 다른 필드들을 더 추가해서 비교를 할수도 있다.
 * 예) @EqualsAndHashCode(of = {"id", "name"})
 * 하지만 절대로 연관관계에 있는 필드는 추가해서는 안된다.
 * 예를 들어 Account 객체와 연관관계가 있을 경우
 * @EqualsAndHashCode(of = {"id", "account"})와 같이
 * 서로 상호참조하는 필드는 추가하면 스택오버플로우가 발생할 수 있다.
 * @Data 또한 @Data 안에 @EqualsAndHashCode를 구현해주는데
 * 모든 필드를 다 사용해서 구현해주기 때문에 엔티티에는 @Data를 사용하면 안된다.
 */
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING) // enum 순서가 바뀔수도 있기 때문에 문자열로.
    private EventStatus eventStatus = EventStatus.DRAFT;

    /**
     * Event를 사용할 때의 Account 정보는 아이디 외에 다른 정보는 필요없기 때문에
     * (특히 패스워드 같은 정보들도 Event 객체를 사용할 때 같이 노출되므로..)
     * AccountSerializer를 만들어서 ObjectMapper가 Account를 JSON으로 변환할 때
     * id 정보만 JSON 내용에 넣도록 한다.
     * 그리고 Account가 다른곳에서 (회원정보 수정과 같은..) 사용될 때에는 Account의 모든 정보가 필요하고
     * Event 객체 내에서만 Account의 다른 정보는 가려지고 id만 JSON으로 변환되도록
     * 아래와 같이 Event 내의 Account 필드에
     * '@JsonSerialize(using = AccountSerializer.class)' 애노테이션을 붙여준다.
     * 이 애노테이션을 붙인 후 createEvent를 요청하면 반환되는 JSON 내용에 Account의 id만 노출되는 것을 확인할 수 있다.
     * (변경전에는 패스워드까지 노출되어 문제가 되었었다.)
     */
    @ManyToOne @JsonSerialize(using = AccountSerializer.class)
    private Account manager; // 이벤트 관리자.

    public void update() {
        // Update free
        // 가격이 있으면 공짜가 아니다.
        if(this.basePrice == 0 && this.maxPrice ==0) {
            this.free = true;
        } else {
            this.free = false;
        }

        // Update offline
        /**
         * isBlank() => 자바 11버전에 추가된 메소드.
         * 이전에는 trim()을 한 후 isEmpty()로 확인했었는데 이젠 isBlank()만 사용하면 됨.
         * 스페이스바 공백 외에도 다른 공백 문자열들이 있는데 모두 확인해줌.
         */
        if(this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }

}
