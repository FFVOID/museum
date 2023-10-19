package com.museum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museum.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByUserId(String userId);
	
	Member findByEmail(String email);
}
