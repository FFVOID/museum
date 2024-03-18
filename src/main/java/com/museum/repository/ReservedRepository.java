package com.museum.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.museum.entity.Reservation;
import com.museum.entity.Reserved;

public interface ReservedRepository extends JpaRepository<Reserved, Long>{
	
	//현재 로그인한 사용자의 예약 데이터를 페이징 조건에 맞춰서 조회
	@Query("select o from Reserved o where o.member.userId = :userId order by o.reservedDate desc")
	List<Reserved> findReserveds(@Param("userId") String userId, Pageable pageable);
	
	//현재 로그인한 회원의 예약 개수가 몇개인지 조회(total)
	@Query("select count(o) from Reserved o where o.member.userId = :userId")
	Long countReserveds(@Param("userId") String userId);
	
	@Query("select o from Reserved o where o.Id = :Id")
	List<Reserved> findReservedId(@Param("Id") Long reservedId);
	
	@Query("select o from Reservation o where o.reserved.id = :Id")
	Reservation findReservation(@Param("Id") Long reservedId);
	
	@Query("select o from Reserved o where o.Id = :Id")
	Reserved findReservedItems(@Param("Id") Long reservedId);
	
}
