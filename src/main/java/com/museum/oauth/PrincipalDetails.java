package com.museum.oauth;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.museum.entity.Member;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private Member member;
	private Map<String, Object> attributes;
	private Collection<? extends GrantedAuthority> authorities;
	 
	public PrincipalDetails(Member member) {
	        this.member = member;
	}
	 
	public PrincipalDetails (Member member, Oauth2UserInfo oAuth2UserInfo) {
	        //PrincipalOauth2UserService 참고
	        this.member = member;
	        this.attributes = oAuth2UserInfo.getAttributes();
	        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));
	}
	 
	@Override
	public Map<String, Object> getAttributes() {
		 return attributes;
	}

	@Override
	public String getName() {
		String sub = attributes.get("sub").toString();
      return sub;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	 	return authorities;
    }

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getName();
	}
	
	public String getEmail() {
		return member.getEmail();
	}
	
	public String getProvider() {
		return member.getProvider();
	}
	
	public String getProviderId() {
		return member.getProviderId();
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
