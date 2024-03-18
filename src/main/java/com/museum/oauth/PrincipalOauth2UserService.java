package com.museum.oauth;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.museum.constant.Role;
import com.museum.entity.Member;
import com.museum.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		Oauth2UserInfo oAuth2UserInfo = null;
		
		String provider = userRequest.getClientRegistration().getRegistrationId();
		
		if("kakao".equals(provider)) { 
    	    oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes()); 
    	} else {
    	    // 적절한 예외 처리 또는 로깅
    	    throw new RuntimeException("지원하지 않는 OAuth 제공자입니다.");
    	}
		  
	    String providerId = oAuth2UserInfo.getProviderId();
	    String username = oAuth2UserInfo.getName();  	
	    String uuid = UUID.randomUUID().toString().substring(0, 6);
	    String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다
		 
	    String email = oAuth2UserInfo.getEmail();
	    Role role = Role.USER;
	     
	    Member byUsername = memberRepository.findByEmail(email);
	     
	    //DB에 없는 사용자라면 회원가입처리
	    if(byUsername == null){
	         byUsername = Member.oauth2Register()
	                 .name(username).password(password).email(email).role(role)
	                 .provider(provider).providerId(providerId)
	                 .build();
	    }else {
	    	    role = byUsername.getRole();
	    }
	     
	    return new PrincipalDetails(byUsername, oAuth2UserInfo);
	}
}
