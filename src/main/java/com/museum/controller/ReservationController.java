package com.museum.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.museum.dto.NewItemDto;
import com.museum.dto.ReservedDto;
import com.museum.dto.ReservedHistDto;
import com.museum.entity.Item;
import com.museum.repository.ItemRepository;
import com.museum.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	private final ItemRepository itemRepository;
	
	
	//전시 예약 페이지
	@GetMapping(value = "/reservation/item")
	public String reservationItem(Model model) {
		
		//예약 가능한 전시 목록 가져와서 보여주기
		List<NewItemDto> newItemDto = reservationService.getItemList();
		
	    model.addAttribute("newItemDto", newItemDto);
	    
		return "reservation/item";
	}
	
	//전시 예약 기능
	@PostMapping(value = "/reservation/item")
	public @ResponseBody ResponseEntity reserved(@RequestBody @Valid ReservedDto reservedDto, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage()); // 에러메세지를 합침
			}
			
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		String userId = principal.getName();
		Long reservedId;
		
		try {
			reservedId = reservationService.reserved(reservedDto, userId); //예약
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Long>(reservedId, HttpStatus.OK);
	}
	
	
	//예약리스트
	@GetMapping(value = {"/reservation/list", "/reservation/list/{page}"})
	public String reservationList(ReservedHistDto reservedHistDto, @PathVariable("page") Optional<Integer> page,Principal principal, Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		
		Page<ReservedHistDto> reservedHistDtoList = reservationService.getReservedList(principal.getName(), pageable);
		
		model.addAttribute("reserved", reservedHistDtoList);
		model.addAttribute("maxPage", 5);
		
		return "reservation/list";
	}
	
	//예약 내역 상세 페이지
	@GetMapping(value = "/reservation/dtl/{reservedId}")
	public String reservedDtl(ReservedHistDto reservedHistDto, @PathVariable("reservedId") Long reservedId,Model model) {
		
		List<ReservedHistDto> reservedHistDtoList = reservationService.getReservedHistDtoList(reservedId);
		
		model.addAttribute("reserved", reservedHistDtoList);

		return "reservation/dtl";
	}
	
	//예약 수정 페이지
	@GetMapping(value = "/reservation/update/{reservedId}")
	public String reservedDtlItem(@PathVariable("reservedId") Long reservedId, Model model) {
		
		ReservedDto reservedDto = reservationService.getReservedItem(reservedId);
		
		Item item = itemRepository.findById(reservedDto.getItemId()).orElse(null);
		model.addAttribute("item", item);
		
		model.addAttribute("reserved", reservedDto);
		
		List<NewItemDto> newItemDto = reservationService.getItemList();
		
		model.addAttribute("newItemDto", newItemDto);
		
		return "reservation/modify";
	}
	
	//예약 수정 기능
	@PostMapping(value = "/reservation/update/{reservedId}")
	public String reservedUpdate(@Valid ReservedDto reservedDto, BindingResult bindingResult,Model model, ReservedHistDto reservedHistDto) {
		
		if(bindingResult.hasErrors()) {
			return "main";
		}	

		try {
			reservationService.updateReserved(reservedHistDto,reservedDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessgae", "예약 수정 중 에러가 발생했습니다");
			return "reservation/item";
		}
		
			return "redirect:/";
		
	}
	
	//예약취소
	@DeleteMapping("/reservation/dtl/{reservedId}/delete")
	public @ResponseBody ResponseEntity deleteReservation(@PathVariable("reservedId") Long reservedId) {
		
		reservationService.deleteReservation(reservedId);
		
		return new ResponseEntity<Long>(reservedId, HttpStatus.OK);
	}
	
	
}
