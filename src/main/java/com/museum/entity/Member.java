package com.museum.entity;




import org.springframework.security.crypto.password.PasswordEncoder;

import com.museum.constant.Role;
import com.museum.dto.MemberFormDto;
import com.museum.dto.SocialMemberDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {
	
	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String userId; //사이트에서 사용할 아이디(로그인 포함)
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String name; //카카오에서 주는 닉네임
	
	private String provider; // 카카오

	private String providerId; // 카카오 기본키 id값
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		
		Member member = new Member();
		
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		
		member.setEmail(memberFormDto.getEmail());
		member.setUserId(memberFormDto.getUserId());
		member.setPassword(password);
		// member.setRole(Role.ADMIN); // 관리자로 가입 할때
		member.setRole(Role.ADMIN);
		
		return member;
	}
	
	public static Member createSocialMember(SocialMemberDto socialMemberDto, PasswordEncoder passwordEncoder) {
		
		Member member = new Member();
		
		String password = passwordEncoder.encode(socialMemberDto.getPassword());
		member.setUserId(socialMemberDto.getUserId());
		member.setEmail(socialMemberDto.getEmail());
		member.setPassword(password);
		
		member.setName(socialMemberDto.getName());
		member.setProvider(socialMemberDto.getProvider());
		member.setProviderId(socialMemberDto.getProviderId());
		member.setRole(Role.USER);
		
		return member;
	}
	
	@Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
	public Member(String name, String password, String email, Role role, String provider, String providerId) {
		
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
	}
	
	public Member() {
    }
}
