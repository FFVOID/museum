package com.museum.controller;


import java.security.Principal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.museum.dto.DeleteMemberDto;
import com.museum.dto.MemberFormDto;
import com.museum.dto.SocialMemberDto;
import com.museum.entity.Member;
import com.museum.repository.MemberRepository;
import com.museum.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	
	//로그인 화면
	@GetMapping(value = "/members/login")
	public String login() {
		return "member/memLoginForm";
	}
	
	//회원가입 화면
	@GetMapping(value = "/members/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}
	
	//회원가입
	@PostMapping(value = "/members/new")
	public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "member/memberForm";
		}
		
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage" , e.getMessage());
			return "member/memberForm";
		}
		
		return "redirect:/";
	}
	
	//로그인 실패
	@GetMapping(value = "/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
		return "member/memLoginForm";
	}
	
	
	/*소셜 가입 처리*/
	
	//소셜 회원가입 화면
	@GetMapping(value = "/members/newSocial")
	public String snsMemberForm( HttpServletResponse response, HttpServletRequest request, Model model) {
		SocialMemberDto socialMemberDto = new SocialMemberDto();

		Cookie[] cookies = request.getCookies();
		
		String email = null;
		String provider = null;
		String providerId = null;
		String name = null;

			for (Cookie cookie : cookies) {
				switch (cookie.getName()) {
				case "email":
					email = cookie.getValue();
					break;
				case "provider":
					provider = cookie.getValue();
					break;
				case "providerId":
					providerId = cookie.getValue();
					break;
				case "name":
					name = cookie.getValue();
					break;
				}
			}
			
		socialMemberDto.setEmail(email);
		socialMemberDto.setProvider(provider);
		socialMemberDto.setProviderId(providerId);
		socialMemberDto.setName(name);
		
		//쿠키정보를 저장했으면 쿠키를 삭제한다 삭제안하면 웹사이트개발자도구에 남아있음
		Cookie emailCookie = new Cookie("email", null);
		emailCookie.setMaxAge(0); 
		emailCookie.setPath("/"); 
		response.addCookie(emailCookie); 
		
		Cookie providerCookie = new Cookie("provider", null); 
		providerCookie.setMaxAge(0); 
		providerCookie.setPath("/"); 
		response.addCookie(providerCookie); 
		
		Cookie providerIdCookie = new Cookie("providerId", null); 
		providerIdCookie.setMaxAge(0);
		providerIdCookie.setPath("/"); 
		response.addCookie(providerIdCookie); 
		
		Cookie nameCookie = new Cookie("name", null); 
		nameCookie.setMaxAge(0); 
		nameCookie.setPath("/"); 
		response.addCookie(nameCookie); 
		
		model.addAttribute("socialMemberDto", socialMemberDto);

		return "member/socialMemberForm";
	}
	
	//소셜 회원가입 기능
	@PostMapping(value = "/members/newSocial")
	public String socialMemberForm(@Valid SocialMemberDto socialMemberDto, 
		   BindingResult bindingResult, Model model) {
		
		//에러처리
		if(bindingResult.hasErrors()) {
			return "member/socialMemberForm";
		}
		
		try {
			Member member = Member.createSocialMember(socialMemberDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage" , e.getMessage());
			return "member/socialMemberForm";
		}
		
		return "redirect:/";
	}
	
	//탈퇴 페이지
	@GetMapping(value = "/members/delMember")
	public String deleteMember(Model model, Principal principal) {
		
		String userId = principal.getName();
		
		Member member = memberRepository.findByUserId(userId);
		
		DeleteMemberDto delMemberDto = new DeleteMemberDto();
		
		delMemberDto.setUserId(member.getUserId());
		
		model.addAttribute("delMemberDto", delMemberDto);
		
		return "member/deleteMemberForm";
	}
	
	//탈퇴기능
	@PostMapping("/members/delMember")
	public String deleteMember(@ModelAttribute DeleteMemberDto delMemberDto, Principal principal, Model model) {
	    String userId = principal.getName();
	    
	    Member member = memberRepository.findByUserId(userId);

	    //입력한 비밀번호와 사용자가 입력한 비번을 비교
	    if (passwordEncoder.matches(delMemberDto.getPassword(), member.getPassword())) {
	        
	    	//일치하면 탈퇴진행
	    	memberService.deleteMember(member.getId());
	    	
	        SecurityContextHolder.clearContext(); // 로그아웃
	        return "redirect:/members/logout"; 
	    } else {
			
	    	DeleteMemberDto delMemberDto2 = new DeleteMemberDto();
	    	
			delMemberDto2.setUserId(member.getUserId());
			
			model.addAttribute("delMemberDto", delMemberDto2);
			model.addAttribute("deleteFailMessage", "탈퇴 실패 비밀번호를 확인해주세요.");
	    	
	        return "member/deleteMemberForm";
	    }
	}
	
}
