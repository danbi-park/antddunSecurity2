package com.ds.antddun.config.oauth;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.entity.Member;
import com.ds.antddun.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

//여기서 후처리가됨
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration:" + userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인 했는지 확인 가능.
        System.out.println("getAccessToken:" + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttribute:" + oAuth2User.getAttributes());

        //회원가입 시킴
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
//        String username = provider + "_" + providerId; // google_109742856182916427686
        String username = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String password = bCryptPasswordEncoder.encode(providerId); //uuid를 비밀번호로 넣어둠 -> 고유
        String role = "ROLE_USER";

        Member memberEntity = memberRepository.findByUsername(username);
        if (memberEntity == null) {
            System.out.println("initial google log in.");
            memberEntity = Member.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .role(role)
                    .build();
            memberRepository.save(memberEntity);
        } else {
            System.out.println("you have already logged in google");
        }
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}
