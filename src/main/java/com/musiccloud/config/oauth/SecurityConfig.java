package com.musiccloud.config.oauth;

import com.musiccloud.enumerated.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2 console 화면을 사용을 위해 disable 해야 한다.
        .and()
                .authorizeRequests() // URL별 관리 설정 옵션의 시작점
                                     // authorizeRequests가 있어야 antMatchers 옵선 사용가능
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // 권한 관리대상 지정 옵션
                                                                         // URL, HTTP 메서드별 관리 가능
                                                                         // "/"등 지정된 URL들은 permitALL() 옵션을 통해 전체 열람 권한을 부여
                                                                         // POST 메서드이면서 "/api/v1/**" 주소를 가진 API는 USER권한을 가진 사람만 가능하게 부여
                .anyRequest().authenticated() // 설정된 값들 이외 나머지 URL
                                              // authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용. 즉 로그인 된 사용자
        .and()
                .logout()
                .logoutSuccessUrl("/") // 로그아웃 성공 시 해당 URL로 이동
        .and()
                .oauth2Login() // OAuth 기능에 대한 여러 설정의 진입점
                    .userInfoEndpoint() // OAuth2 로그인 성공 후 사용자 정보를 가져올 때의 설정
                        .userService(customOAuth2UserService);

    }
}
