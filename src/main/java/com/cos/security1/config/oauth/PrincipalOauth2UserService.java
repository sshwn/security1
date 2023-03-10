package com.cos.security1.config.oauth;

import com.cos.security1.config.CustomBCryptPasswordEncoder;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.NaverUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private CustomBCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest:"+userRequest); // userRequest:org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest@4a0dcaf8
        System.out.println("getClientRegistration:"+userRequest.getClientRegistration());   // registrationId='google' 로 어디서 로그인했는지 확인가능 ClientRegistration{registrationId='google', clientId='31849756497-ebjd3nuesqimtd0f4v7go4lahkvl771j.apps.googleusercontent.com', clientSecret='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', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@3ea219cf, clientName='Google'}
        System.out.println("getAccessToken:"+userRequest.getAccessToken()); // org.springframework.security.oauth2.core.OAuth2AccessToken@9834117b
        System.out.println("getTokenValue:"+userRequest.getAccessToken().getTokenValue());  // ya29.a0AX9GBdXJ8yey7IN1IcyqTprE5YMwlkc8yWe_hWi7L26istO6pXYZTcdXpmo-reMnRibP22LVvOwZTDEjV5wMWU79MLYXCpNQAp89W78DZ6nWKs_seobz6EfhzvKVDKR5-zMNGZrNspItOl-w4SJRLfGyZrRgaCgYKAcsSARMSFQHUCsbC5abOcfxjpIGFLybXqR_a9A0163


        /**
         * username = "google_109208456048493571184"
         * password = 암호화(겟인데어)
         * email = "nhw0926@gmail.com"
         * role = "ROLE_USER"
         * provider = "google"
         * providerId = "109208456048493571184"
         */

        OAuth2User oauth2User = super.loadUser(userRequest);

        // userRequest정보 : 구글로그인 버튼 클릭 -> 구글로그인 창 -> 로그인을 완료하면 -> code를 리턴(OAuth-Client라이브러리) -> Access ToKen 요청
        // userRequest정보 => loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes()); // {sub=109208456048493571184, name=나현우, given_name=현우, family_name=나, picture=https://lh3.googleusercontent.com/a/AEdFTp66iUD785qhwUyttkrNkKrdtmBpm5j4FatBoSP8=s96-c, email=nhw0926@gmail.com, email_verified=true, locale=ko}

        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
        } else {
            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎㅎ");
        }

        String provider = oAuth2UserInfo.getProvider();    // google , facebook
        String providerId = oAuth2UserInfo.getProviderId(); // google:sub , facebook:id
        String username = provider+"_"+providerId; //google_109208456048493571184
        String password = bCryptPasswordEncoder.encode("겟인데어");
//        String password = "테스트";
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            System.out.println(provider+" 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
        }
//        String username = oAuth2User.getAttribute()

        // 회원가입을 강제로 진행해볼 예정

//        return super.loadUser(userRequest);
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
        // OAuth 로 로그인했을때 override한 이유는 PrincipalDetails 타입으로 묶기위해서이고, OAuth로 로그인했을때 회원가입을 강제로 진행하기위해
    }
}
