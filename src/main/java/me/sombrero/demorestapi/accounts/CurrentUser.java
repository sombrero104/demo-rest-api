package me.sombrero.demorestapi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AuthenticationPrincipal 애노테이션 사용 시..
 * 익명사용자 anonymousUser일 경우에는 문자열로 들어오기 때문에
 * 컨트롤러 파라미터에서 AccountAdapter의 account 필드를 가져올 때
 * 문자열에는 AccountAdapter 객체처럼 getAccount()를 가지고 있지 않기 때문에
 * anonymousUser 문자열이 들어오면 에러가 발생한다.
 * 때문에 아래와 같이 @AuthenticationPrincipal을 정의한다.
 *      @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
