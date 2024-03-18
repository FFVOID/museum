package com.museum.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.museum.entity.Member;
import com.museum.oauth.PrincipalDetails;
import com.museum.repository.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private final MemberRepository memberRepository;
	
	// 간편 로그인 성공시 정보를 sns전용 회원가입 페이지로 전달
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2User oauthUser = ((OAuth2AuthenticationToken) authentication).getPrincipal();
			
			if (oauthUser instanceof PrincipalDetails) {
				PrincipalDetails userDetails = (PrincipalDetails) oauthUser;
				
				String email1 = userDetails.getEmail();
				String provider1 = userDetails.getProvider();
				String providerId1 = userDetails.getProviderId();
				String name1 = userDetails.getUsername();
				Member member = memberRepository.findByEmail(email1);

				if (member == null) {
					
					SecurityContextHolder.getContext().setAuthentication(null);
					Cookie email = new Cookie("email", email1);
					email.setDomain("localhost");
					email.setPath("/");
	                Cookie provider = new Cookie("provider", provider1);
	                provider.setDomain("localhost");
	                provider.setPath("/");
	                Cookie providerId = new Cookie("providerId", providerId1);
	                providerId.setDomain("localhost");
	                providerId.setPath("/");
	                Cookie name  = new Cookie("name", name1);
	                name.setDomain("localhost");
	                name.setPath("/");
	                
	                response.addCookie(email);
	                response.addCookie(provider);
	                response.addCookie(providerId);
	                response.addCookie(name);

	                response.sendRedirect("/members/newSocial");

				} else {
					
					HttpSession session = request.getSession();
					Long memberId = member.getId();
					String email = member.getEmail();
					String role = member.getRole().toString();

					session.setAttribute("memberId", memberId);
	                session.setAttribute("email", email);
	                session.setAttribute("role", role);
	                
	                session.setAttribute("isTemporary", false);
					response.sendRedirect("/");
				}
			}
		}

	}
	
}
