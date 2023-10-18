package com.museum.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.museum.dto.ReservedHistDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reserved")
@Getter
@Setter
@ToString
public class Reserved extends BaseEntity{
	
	@Id
	@Column(name = "reserved_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//예약완료날짜
	private LocalDateTime reservedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToMany(mappedBy = "reserved" ,cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
	private List<Reservation> reservationList = new ArrayList<>();
	
	public void addReservationList(Reservation reservation) {
		this.reservationList.add(reservation);
		reservation.setReserved(this);
	}
	
	//예약생성(예약완료)
	public static Reserved createReserved(Member member, List<Reservation> reservationItemList) {
		Reserved reserved = new Reserved();
		reserved.setMember(member);
		
		for(Reservation reservationList : reservationItemList) {
			reserved.addReservationList(reservationList);
		}
		
		reserved.setReservedDate(LocalDateTime.now());
		
		return reserved;
	}
	
	public void updateReserved(ReservedHistDto reservedHistDto) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
		
		LocalDateTime date = LocalDateTime.parse(reservedHistDto.getReservedDate(), formatter);
		
		this.reservedDate = date;
		
	}
}
