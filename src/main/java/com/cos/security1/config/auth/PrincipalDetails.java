package com.cos.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 session을 만들어준다. (Security ContextHolder 에 세션정보를 저장한다.)
// 오브젝트타입 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 됨.
// User오브젝트타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;  // 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {  // 계정 만료되었니?
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {   // 계정 잠겻니?
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 계정 비밀번호 만료기간 지낫니?
        return true;
    }

    @Override
    public boolean isEnabled() {    // 계정이 활성화되있니?
        // 우리 사이트!! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로 함.
        // 현재시간 - 마지막 로그인시간 => 1년을 초과하면 return false;
        return true;
    }
}