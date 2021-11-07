package com.ds.antddun.config.oauth;

import com.ds.antddun.config.auth.PrincipalDetails;
import com.ds.antddun.config.oauth.provider.FacebookUserInfo;
import com.ds.antddun.config.oauth.provider.GoogleUserInfo;
import com.ds.antddun.config.oauth.provider.OAuth2UserInfo;
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

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("우리는 구글과 페이스북만 지원해요");
        }

        //회원가입 시킴
        String provider = oAuth2UserInfo.getProvider(); // google
        String providerId = oAuth2UserInfo.getProviderId();
//        String username = provider + "_" + providerId; // google_109742856182916427686
        String username = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String password = bCryptPasswordEncoder.encode(oAuth2UserInfo.getProviderId()); //uuid를 비밀번호로 넣어둠 -> 고유
        String role = "ROLE_USER";

        Member memberEntity = memberRepository.findByUsername(username);
        if (memberEntity == null) {
            System.out.println("initial log in.");
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
            System.out.println("로그인을 한 적이 있습니다.");
        }
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}
