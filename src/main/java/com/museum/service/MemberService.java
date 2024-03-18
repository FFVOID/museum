package com.museum.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.museum.entity.BoardComment;
import com.museum.entity.Member;
import com.museum.repository.BoardCommentRepository;
import com.museum.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional //쿼리문 수행시 에러가 발생하면 변경된 데이터를 이전상태로 콜백시켜줌
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	private final BoardCommentRepository boardCmtRepository;
	
	//회원가입 데이터를 DB에 저장한다
	public Member saveMember(Member member) {
		validateDuplicateMember(member); //중복체크
		Member savedMember = memberRepository.save(member);
		return savedMember;
	}
	
	//아이디 중복체크
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByUserId(member.getUserId());
		
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 아이디입니다. 다른 아이디를 사용해주세요");
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		//사용자가 입력한 email이 DB에 있는지 쿼리문을 사용해서 알아본다
		Member member = memberRepository.findByUserId(userId);
		if(member == null) { //사용자가 없으면
			throw new UsernameNotFoundException(userId);
		}
		
		//사용자가 있다면 DB에서 가져온 값으로 userDetails 객체를 만들어서 반환
		return User.builder()
				   .username(member.getUserId())
				   .password(member.getPassword())
				   .roles(member.getRole().toString())
				   .build();
	}
	
	public boolean findMember(String email) {
		
		boolean ck = true;
		
		Member member = memberRepository.findByEmail(email);
		
		if(member != null) {
			ck = false;
		}
		
		return ck;
	}
	
	//회원탈퇴
	public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        
        memberRepository.delete(member);
    }
}	


