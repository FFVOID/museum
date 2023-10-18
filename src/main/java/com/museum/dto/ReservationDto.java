package com.museum.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;

import com.museum.entity.Reservation;

import lombok.*;

@Getter
@Setter
public class ReservationDto {
	
	public ReservationDto() {};
	
	// view에 보여주기위해 엔티티를 dto로 변환
	public ReservationDto(Reservation reservation) {
		this.itemNm = reservation.getItem().getItemNm();
		this.itemDate = reservation.getItem().getItemDate();
		this.reservedNm = reservation.getReservedNm();
		this.date = reservation.getDate();
		this.count = reservation.getCount();
	}
	
	//전시명
	private String itemNm;
	
	//전시기간
	private String itemDate;
	
	//예약자이름
	private String reservedNm;
	
	//예약날짜
	private LocalDate date;
	
	//예약인원
	private int count;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ReservationDto of (Reservation reservaiton) {
		return modelMapper.map(reservaiton, ReservationDto.class);
	}
}
