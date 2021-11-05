package com.ds.antddun.controller;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class loginController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //일반 로그인 또는 OAuth로그인을 해도 PrincipalDetails 타입으로 받을 수 있음
    @GetMapping("/user")
    public @ResponseBody String user(
            @AuthenticationPrincipal PrincipalDetails principalDetails){ //다운캐스팅 안해도 됨
        System.out.println("principalDetails: " + principalDetails.getMember());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    //SecurityConfig 파일 생성 후 작동안함.
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/test/login")
    public @ResponseBody
    String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails){ //DI 의존성 주입
        System.out.println("/test/login ===========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //다운캐스팅
        System.out.println("authentication" + principalDetails.getMember());
        System.out.println("userDetails:" + userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth){ //DI 의존성 주입
        System.out.println("/test/oauth/login ===========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication" + oAuth2User.getAttributes());
        System.out.println("oauth2User:" + oauth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

}
