package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // secured 어노테이션 활성화 , (preAuthorize, postAuthorize) 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()  // 위 3개의 주소가아니면 어디든 접근가능하다.
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")  // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다. (controller 에 /login을 만들지않아도된다.)
                .defaultSuccessUrl("/") // 로그인 성공 시 / 로 이동하지만 (localhost:8080/user 호출하여 로그인페이지 이동 후 로그인하면 localhost:8080/user 로 내가 호출햇던 페이지로 이동함)
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);   // 구글 로그인이 완룓된 뒤의 처리가 필요함. Tip. 코드를 받지않고 (엑세스토근+사용자프로필정보 를 한번에 받는다)
                // 1.코드받기(인증)[이 사람이 로그인이 되었다. 구글에 로그인이 된 정상적인 사용자다 ], 2.엑세스토근받기(권한)[사용자 정보에 접근가능한 권한], 3. 2에서 받은 권한을 통해 사용자 프로필 정보를 가져와
                // 4-1.그 정보를 토대로 회원가입을 자동으로 진행시키기도 함. 4-2. (이메일, 전화번호, 이름, 아이디) 만 가져왓을때 내가 쇼핑몰을하면 -> 집주소가필요, 백화점몰이면 -> 등급이필요요                //
    }           // 위 내용처럼 추가적인 정보가 필요하다면, 따로 회원가입을 진행해야한다.
}
