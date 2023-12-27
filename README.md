<div>
  <h1>박물관 사이트(개인프로젝트)</h1>
</div>

<div>
  <h2> :bulb: 프로젝트 소개</h2>
	스프링 부트(MVC패턴) + JPA + Mysql를 이용한 박물관 사이트
</div>

<div align="left">
  <h2> :computer: 사용기술</h2>
	<h5>백엔드</h5>
		<img src="https://img.shields.io/badge/Eclipse IDE-2C2255?style=flat&logo=Eclipse IDE&logoColor=white" />
		<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
		<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white" />
  <img src="https://img.shields.io/badge/jQuery-0769AD?style=flat&logo=jQuery&logoColor=white" />
	<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
	<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=white" />
	<h5>프론트</h5>
	<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white" />
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=Bootstrap&logoColor=white" />
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=white" />
	<h5>DB</h5>
	<img src="https://img.shields.io/badge/AWS-232F3E?style=flat&logo=Amazon AWS&logoColor=white" />
 <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
</div>

<div>
  <h2>주요기능</h2>
	  <P>1. 회원가입(이메일을 사용해 인증번호를 이용)</P>
	  <P>2. 카카오api(OAuth2)를 이용한 회원가입</P>
	  <P>3. 전시등록,관리 기능(관리자)</P>
	  <P>4. 전시예약,수정,취소 기능</P>
	  <P>5. 전시상세정보,리스트,검색 기능</P>
	  <P>6. 게시판(CRUD) 글쓰기,수정,삭제 기능(이미지포함)</P>
	  <P>7. 회원탈퇴</P>
</div>

### :blue_book: 아키텍쳐
![아키텍쳐](https://github.com/FFVOID/museum/assets/130435247/f274d2a9-f7a7-42dd-a822-5bbe505797f6)

### :closed_book: ERD 설계도
![ERD](https://github.com/FFVOID/museum/assets/130435247/c2ee98ef-70f0-4a5c-ac1c-a9a60b130493)

### :green_book: 유스케이스 설계도
![유스케이스](https://github.com/FFVOID/museum/assets/130435247/5be48d04-dea3-49e8-815f-b9d0671cd68e)

### :one: 회원가입(유저는 이메일, 비밀번호를 입력하여 가입)
![회원가입1](https://github.com/FFVOID/museum/assets/130435247/11629366-dd15-4055-94fb-562e6c22729a)

### :two: api를 이용한 회원가입(카카오 로그인을 통해 간편가입)
![회원가입2](https://github.com/FFVOID/museum/assets/130435247/f2480fac-7921-4a79-b485-0696a26e0aef)

:lock: 로그인 기능은 스프링 시큐리티를 활용

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
			.logout(logout -> logout //3.로그아웃 설정 
					.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) 
					.logoutSuccessUrl("/"))
			.exceptionHandling(handling -> handling // 4. 인증되지 않은 사용자가 리소스에 접근했을때 설정
					.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
			.rememberMe(Customizer.withDefaults());
			return http.build();
	}
 


### :three: 전시등록,관리(관리자 기능)
![전시관리](https://github.com/FFVOID/museum/assets/130435247/b4207e98-53d3-47c7-bdab-fe80ccc1f7d8)

### :four: 전시예약,취소,수정
![전시예약,취소,수정](https://github.com/FFVOID/museum/assets/130435247/4c84a564-50ca-4a46-b61f-c1e57f35e01b)

### :five: 전시리스트,검색
![전시리스트,검색](https://github.com/FFVOID/museum/assets/130435247/270d1a89-c1f7-4e8e-a7d9-b50656e72d71)

### :six: 게시판 글쓰기,수정,삭제 기능(이미지,댓글포함)
![게시판글쓰기,수정,삭제(이미지포함,댓글)](https://github.com/FFVOID/museum/assets/130435247/c1ffbb18-79b8-4230-9318-b17dfe044c8f)

### :seven: 회원탈퇴
![회원탈퇴](https://github.com/FFVOID/museum/assets/130435247/15b03598-6800-42ae-ad6f-78f8be1e98be)
