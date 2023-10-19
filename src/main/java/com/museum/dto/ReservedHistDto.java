package com.museum.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.museum.entity.Reserved;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedHistDto {
	
	public ReservedHistDto() {};
	
	public ReservedHistDto(Reserved reserved) {
		this.reservedId = reserved.getId();
		this.reservedDate = reserved.getReservedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		
	}
	
	private Long reservedId;
	
	private String reservedDate;
	
	private List<ReservationDto> reservationDtoList = new ArrayList<>();
	
	public void addReservationDtoList(ReservationDto reservationDto) {
		this.reservationDtoList.add(reservationDto);
	}
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ReservedHistDto of (Reserved reserved) {
		
		return modelMapper.map(reserved, ReservedHistDto.class);
	}
}
