package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest:"+userRequest); // userRequest:org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest@4a0dcaf8
        System.out.println("getClientRegistration:"+userRequest.getClientRegistration());   // registrationId='google' 로 어디서 로그인했는지 확인가능 ClientRegistration{registrationId='google', clientId='31849756497-ebjd3nuesqimtd0f4v7go4lahkvl771j.apps.googleusercontent.com', clientSecret='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', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@3ea219cf, clientName='Google'}
        System.out.println("getAccessToken:"+userRequest.getAccessToken()); // org.springframework.security.oauth2.core.OAuth2AccessToken@9834117b
        System.out.println("getTokenValue:"+userRequest.getAccessToken().getTokenValue());  // ya29.a0AX9GBdXJ8yey7IN1IcyqTprE5YMwlkc8yWe_hWi7L26istO6pXYZTcdXpmo-reMnRibP22LVvOwZTDEjV5wMWU79MLYXCpNQAp89W78DZ6nWKs_seobz6EfhzvKVDKR5-zMNGZrNspItOl-w4SJRLfGyZrRgaCgYKAcsSARMSFQHUCsbC5abOcfxjpIGFLybXqR_a9A0163
        System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes()); // {sub=109208456048493571184, name=나현우, given_name=현우, family_name=나, picture=https://lh3.googleusercontent.com/a/AEdFTp66iUD785qhwUyttkrNkKrdtmBpm5j4FatBoSP8=s96-c, email=nhw0926@gmail.com, email_verified=true, locale=ko}

        /**
         * username = "google_109208456048493571184"
         * password = 암호화(겟인데어)
         * email = "nhw0926@gmail.com"
         * role = "ROLE_USER"
         * provider = "google"
         * providerId = "109208456048493571184"
         */

        // userRequest정보 : 구글로그인 버튼 클릭 -> 구글로그인 창 -> 로그인을 완료하면 -> code를 리턴(OAuth-Client라이브러리) -> Access ToKen 요청
        // userRequest정보 => loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 회원가입을 강제로 진행해볼 예정

        return super.loadUser(userRequest);
    }
}
