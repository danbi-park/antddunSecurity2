package com.ds.antddun.config.auth;

//security가 /login주소 요청이 오면 낚아채서 로그인을 진행
//로그인 진행이 완료되면 security session을 만들어줌(security ContextHolder)
//오브젝트 타입 -> Authentication 타입 객체
//Authentication 안에 User정보가 있어야 됨
//User오브젝트의 타입 -> UserDetails 타입 객체
//security session 에 정보를 저장하는데 들어갈 수 있는 객체 가 Authentication! ->
// 그리고 여기에 유저 정보를 저장할 떼 타입이 UserDetails(PrincipalDetails)여야함

import com.ds.antddun.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private Member member; //콤포지션

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //해당 Member의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
