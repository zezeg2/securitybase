package com.example.securitybase.config.auth;

import com.example.securitybase.config.auth.oauth.provider.FacebookUserInfo;
import com.example.securitybase.config.auth.oauth.provider.GoogleUserInfo;
import com.example.securitybase.config.auth.oauth.provider.NaverUserInfo;
import com.example.securitybase.config.auth.oauth.provider.OAuth2UserInfo;
import com.example.securitybase.domain.user.User;
import com.example.securitybase.domain.user.Role;
import com.example.securitybase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// security 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
// session(내부 Authentication(내부 UserDetails(PrincipalDetails(userEntity)))))
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService extends DefaultOAuth2UserService implements UserDetailsService  {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) return new PrincipalDetails(userEntity); //  PrincipalDetails 타입으로 리턴
        return null;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user  = super.loadUser(userRequest);

        OAuth2UserInfo userinfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            log.info("Google 로그인 요청");
            userinfo = new GoogleUserInfo(user.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            log.info("Facebook 로그인 요청");
            userinfo = new FacebookUserInfo(user.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            log.info("Naver 로그인 요청");
            userinfo = new NaverUserInfo((Map)user.getAttributes().get("response"));
        }

        String provider = userinfo.getProvider();
        String providerId = userinfo.getProviderId();
        String email = userinfo.getEmail();
        String username = userinfo.getName();
        String OAuthId = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("gymcot");

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            log.info(provider + " 로그인이 최초입니다. ");
            userEntity = User.builder()
                    .OAuthId(OAuthId)
                    .email(email)
                    .username(username)
                    .password(password)
                    .role(Role.ROLE_MEMBER)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        //  PrincipalDetails 타입으로 리턴
        return new PrincipalDetails(userEntity, user.getAttributes());
    }
}
