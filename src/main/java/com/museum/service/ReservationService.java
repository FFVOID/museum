package com.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.museum.dto.NewItemDto;
import com.museum.dto.ReservationDto;
import com.museum.dto.ReservedDto;
import com.museum.dto.ReservedHistDto;
import com.museum.entity.Item;
import com.museum.entity.Member;
import com.museum.entity.Reservation;
import com.museum.entity.Reserved;
import com.museum.repository.ItemRepository;
import com.museum.repository.MemberRepository;
import com.museum.repository.ReservationRepository;
import com.museum.repository.ReservedRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final ReservedRepository reservedRepository;
	
	//예약
	public Long reserved(ReservedDto reservedDto , String userId) {
		
		//전시 조회
		Item item = itemRepository.findById(reservedDto.getItemId())
								.orElseThrow(EntityNotFoundException::new);
		
		//현재 로그인한 회원의 아이디와 회원 정보를 조회
		Member member = memberRepository.findByUserId(userId);
		
		//예약할 전시 엔티티(item)를 이용해 예약전시(reservation)엔티티를 작성 
		List<Reservation> reservationItemList = new ArrayList<>();
		
		if (item.getStock() <= 0) {
	        throw new RuntimeException("예약 인원이 다차서 예약이 불가능합니다");
	    }
		
		item.setStock(item.newStock(reservedDto.getCount()));
		
		Reservation reservationItem = Reservation.createReservation(item, reservedDto.getReservedNm(),
				reservedDto.getDate(), reservedDto.getCount());
		reservationItemList.add(reservationItem);
		
		//회원 정보와 예약할 전시 리스트 정보를 이용하여 예약 엔티티를 생성
		Reserved reserved = Reserved.createReserved(member, reservationItemList);
		reservedRepository.save(reserved);
		
		return reserved.getId();
	}
	
	//예약페이지에 전시목록 가져오기
	@Transactional(readOnly = true)
	public List<NewItemDto> getItemList(){
		
		List<Item> itemList = itemRepository.findAll();
		
		List<NewItemDto> itemDtoList = new ArrayList<>();
		
		for(Item itmes : itemList) {
			NewItemDto newItemDto = NewItemDto.of(itmes);
			
			itemDtoList.add(newItemDto);
		}
		
		return itemDtoList;
	}
	
	//예약목록 가져오기
	@Transactional(readOnly = true)
	public Page<ReservedHistDto> getReservedList(String userId, Pageable pageable){
		
		List<Reserved> reservedList = reservedRepository.findReserveds(userId, pageable);
		
		Long totalCount = reservedRepository.countReserveds(userId);
		
		List<ReservedHistDto> reservedHistDtoList = new ArrayList<>();
		
		for(Reserved reserved : reservedList) {
			ReservedHistDto reservedHistDto = new ReservedHistDto(reserved);
			
			List<Reservation> reservations = reserved.getReservationList();
			
			for(Reservation reservationItem : reservations) {
				ReservationDto reservationDto = new ReservationDto(reservationItem);
				reservedHistDto.addReservationDtoList(reservationDto);
			}
			
			reservedHistDtoList.add(reservedHistDto);
			
		}
		
		return new PageImpl<>(reservedHistDtoList, pageable, totalCount);
	}
	
	//예약 정보(상세내역용) 가져오기 
	@Transactional(readOnly = true)
	public List<ReservedHistDto> getReservedHistDtoList(Long reservedId){
		
		List<Reserved> reservedList = reservedRepository.findReservedId(reservedId);
		
		List<ReservedHistDto> reservedHistDtoList = new ArrayList<>();
		
		for(Reserved reserved : reservedList) {
			ReservedHistDto reservedHistDto = new ReservedHistDto(reserved);
			
			List<Reservation> reservations = reserved.getReservationList();
			
			for(Reservation reservationItem : reservations) {
				ReservationDto reservationDto = new ReservationDto(reservationItem);
				reservedHistDto.addReservationDtoList(reservationDto);
			}
			
			reservedHistDtoList.add(reservedHistDto);
			
		}
		
		return reservedHistDtoList;
	}
	
	
	//수정페이지에 수정할 예약 정보가져오기
	@Transactional(readOnly = true)
	public ReservedDto getReservedItem(Long reservedId){
		
		Reservation reservation = reservedRepository.findReservation(reservedId);
		
		//생성자를 이용 ReservationDto reservationDto = new ResrvationDto(reservation);
		//modelmapper를 이용해서 앞단에 정보 보여줌
		//ReservationDto reservationDto = ReservationDto.of(reservation);
		ReservedDto reservedDto = ReservedDto.of(reservation);
		
		return reservedDto;
	}
	
	//예약 수정
	@Transactional
	public Long updateReserved(ReservedHistDto reservedHistDto ,ReservedDto reservedDto) throws Exception {
		
		Reservation reservation = reservedRepository.findReservation(reservedHistDto.getReservedId());
		//Reserved reserved = reservedRepository.findReservedIds(reservedId);
		
		int preCount = reservation.getCount();
		System.out.println(preCount);
	    int currentCount = reservedDto.getCount();
	    System.out.println(currentCount);
	    int reCount = currentCount - preCount;
	    System.out.println(reCount);
		
	    Item item = reservation.getItem();
	    int newStock = item.getStock() - reCount;
	    if (newStock < 0) {
	        throw new RuntimeException("예약이 불가능합니다");
	    }
	    
	    item.setStock(newStock);
	    
	    itemRepository.save(item);
	    
		reservation.updateReservation(reservedDto);
		//reserved.updateReserved(reservedHistDto);
		
		return reservation.getId();
		
	}
	
	//예약취소
	public void deleteReservation(Long reservedId) {
		Reserved reserved = reservedRepository.findById(reservedId)
											.orElseThrow(EntityNotFoundException::new);
		
		Reservation reservation = reservedRepository.findReservation(reservedId);
		
		Item item = reservation.getItem();
		
		int preCount = reservation.getCount();
	    int currentCount = reservation.getItem().getStock();
	    int reCount = currentCount + preCount;
	    
	    item.setStock(reCount);
		
		itemRepository.save(item);
		
		reservedRepository.delete(reserved);
	}
	
}
