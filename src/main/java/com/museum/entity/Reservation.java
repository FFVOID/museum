package com.museum.entity;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.museum.dto.ReservedDto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="reservation")
@Getter
@Setter
@ToString
public class Reservation extends BaseEntity {
	
	//예약 식별자 아이디
	@Id
	@Column(name="reservation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	//예약자이름
	private String reservedNm;
	
	//예약날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	//예약인원
	private int count;
	
	//전시식별자
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	//회원 식별자 아이디
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="reserved_id")
	private Reserved reserved;
	
	//예약생성
	public static Reservation createReservation(Item item , String reservedNm, LocalDate date, int count) {
		
		Reservation reservation = new Reservation();
		
		reservation.setItem(item);
		reservation.setReservedNm(reservedNm);
		reservation.setDate(date);
		reservation.setCount(count);
		
		return reservation;
	}
	
	public void updateReservation(ReservedDto reservedDto) {
		
		this.count = reservedDto.getCount();
		this.reservedNm = reservedDto.getReservedNm();
		this.date = reservedDto.getDate();
	}
}
