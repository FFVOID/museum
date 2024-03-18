package com.museum.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.museum.entity.Reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedDto {
	
	@NotNull(message = "전시 아이디는 필수 입력입니다")
	private Long itemId;
	
	@NotNull(message = "예약자명은 필수입니다")
	private String reservedNm;
	
	//전시예약날짜
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	private int count;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ReservedDto of (Reservation reservation) {
		
		Reservation reservations = new Reservation();
		return modelMapper.map(reservation, ReservedDto.class);
	}
}
