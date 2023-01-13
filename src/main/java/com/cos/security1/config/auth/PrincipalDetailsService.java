package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행된다. (프로그램 규칙) [loginForm.html 의 action="/login"이 호출되면 스프링은 IoC컨테이너에서 UserDetailsService를찾는다. 찾고 바로 loadUserByUsername 함수를 호출한다.]
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session내부에(Authentication(내부에 UserDetails 가 들어간다))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // String username의 경우 loginForm.html 의 username 과 매핑된다.
        // 만약 loginForm.html의 username 이 username2로 변경된다면, SecurityConfig.java에서 .usernameParameter("username2") 설정을해줘야한다.

        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
