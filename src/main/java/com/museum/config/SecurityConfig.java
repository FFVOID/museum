package com.museum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.museum.handler.LoginSuccessHandler;
import com.museum.oauth.PrincipalOauth2UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Lazy
	public SecurityConfig(PrincipalOauth2UserService principalOauth2UserService) {
		this.principalOauth2UserService = principalOauth2UserService;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//로그인에 대한 설정
		http.authorizeHttpRequests(authorize -> authorize //페이지 접근권한에 관한 설정
					.requestMatchers("/css/**" , "/js/**" , "/img/**" ,"/webfonts/**" ).permitAll()
					.requestMatchers("/", "/members/**", "/exhibition/**" , "/email/**").permitAll()
					.requestMatchers("/favicon.ico", "/error").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2 
					.loginPage("/members/login")
					.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(principalOauth2UserService))
					.successHandler(loginSuccessHandler)
					.failureUrl("/members/login/error")
					)
			.formLogin(formLogin -> formLogin 
					 .loginPage("/members/login")
					 .defaultSuccessUrl("/") 
					 .usernameParameter("userId")
					 .failureUrl("/members/login/error"))
			.logout(logout -> logout //로그아웃 설정 
					.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
					.logoutSuccessUrl("/"))
			.exceptionHandling(handling -> handling //인증되지 않은 사용자가 리소스에 접근했을때 설정
					.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
			.rememberMe(Customizer.withDefaults());
		
			return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
