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
				//모든 사용자가 로그인(인증)없이 접근할 수 있도록 설정
				.requestMatchers("/css/**" , "/js/**" , "/img/**" ,"/webfonts/**" ).permitAll()
				.requestMatchers("/", "/members/**", "/exhibition/**" , "/email/**").permitAll()
				.requestMatchers("/favicon.ico", "/error").permitAll()
				//'admin'으로 시작하는 경로는 관리자만 접근가능하도록 설정
				.requestMatchers("/admin/**").hasRole("ADMIN")
				//그 외 페이지는 모두 로그인(인증을 받아야함)
				.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2 
					.loginPage("/members/login")
					.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(principalOauth2UserService))
					.successHandler(loginSuccessHandler)
					.failureUrl("/members/login/error")
					)
			.formLogin(formLogin -> formLogin //로그인에 관련된 설정
						 .loginPage("/members/login") //로그인 페이지 url
						 .defaultSuccessUrl("/") //로그인 성공시 이동할 url
						 .usernameParameter("userId")
						 .failureUrl("/members/login/error"))
			.logout(logout -> logout //3.로그아웃 설정 
					.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃시 이동할 url
					.logoutSuccessUrl("/"))
			.exceptionHandling(handling -> handling // 4. 인증되지 않은 사용자가 리소스에 접근했을때 설정(ex. 로그인 안했는데 cart페이지에 접근...)
					.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
			.rememberMe(Customizer.withDefaults());
		
			return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
