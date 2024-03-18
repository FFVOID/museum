package com.museum.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String>{

	@Override
	public Optional<String> getCurrentAuditor() {
		//현재 로그인한 사용자의 정보를 가져온다
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "";
		
		if(authentication != null) {
			userId = authentication.getName();
		}
		
		return Optional.of(userId);
	}
	
	
}
