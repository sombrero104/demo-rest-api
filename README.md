# REST API
<br/>

# REST API 보안 적용
<br/>

## 스프링 시큐리티
스프링 시큐리티 의존성을 추가하여 사용하는 순간부터 <br/>
모든 요청은 인증을 필요로 하게 된다. <br/>
그리고 임의의 사용자 하나를 인메모리에 만들어준다. <br/>
<br/>

## 스프링 시큐리티 OAuth 2.0
### AuthorizationServer
OAuth2 토큰 발행(/oauth/token) 및 토큰 인증(/oauth/authorize)
> Order 0 (리소스 서버보다 우선순위가 높다.)
### ResourceServer
리소스 요청 인증 처리 (OAuth2 토큰 검사)
> Order 3 (이 값은 현재 고칠 수 없음.)

## 스프링 시큐리티 OAuth 2.0 적용 
1. 먼저 AuthorizationServer와 ResourceServer가 공통으로 사용할 설정을 추가해야 한다. <br/>
=> me.sombrero.demorestapi.configs.SecurityConfig.java 파일 참조. <br/>
<br/>
2. UserDetailsService를 상속받는 AccountService를 만든다.<br/>
(DB에서 username으로 찾아서 꺼내온 사용자 정보를<br/>
UserDetails로 반환하는 loadUserByUsername()을 구현한다.)<br/>
=> me.sombrero.demorestapi.accounts.AccountService.java 파일 참조. <br/>
<br/>
3. 인증 토큰을 발급하기 위한 설정인 AuthServerConfig를 만든다.<br/>
=> me.sombrero.demorestapi.configs.AuthServerConfig.java 파일 참조. <br/>
<br/>

