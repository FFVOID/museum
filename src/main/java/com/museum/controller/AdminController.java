package com.museum.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.museum.dto.ItemSearchDto;
import com.museum.dto.NewItemDto;
import com.museum.entity.Item;
import com.museum.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final ItemService itemService;
	
	//전시 등록 페이지
	@GetMapping(value = "/admin/newItem")
	public String newItem(Model model) {
		
		model.addAttribute("newItemDto", new NewItemDto());
		return "admin/newItem";
	}
	
	//전시,소장품이미지 등록
	@PostMapping(value = "/admin/newItem")
	public String itemNew(@Valid NewItemDto newItemDto, BindingResult bindingResult, Model model, 
			@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Item items) {
		
		if(bindingResult.hasErrors()) {
			return "admin/newItem";
		}
		
		if(itemImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 소장품 이미지는 필수 입니다");
			return "admin/newItem";
		}
		
		try {
			itemService.saveItem(newItemDto, itemImgFileList, items);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage" , "상품 등록 중 에러가 발생했습니다");
		}
		
		return "redirect:/";
	}
	
	//전시관리페이지
	@GetMapping(value = {"/admin/itemList", "/admin/itemList/{page}"})
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 5);
		
		Page<Item> itemList = itemService.getAdminItemPage(itemSearchDto, pageable);
		
		model.addAttribute("itemList", itemList);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "/admin/itemList";
		
	}
	
	//수정할 전시의 저장된 정보를 불러옴
	@GetMapping(value = "/admin/item/{itemId}")
	public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
		
		try {
			NewItemDto newItemDto = itemService.getItemDtl(itemId);
			model.addAttribute("newItemDto", newItemDto);
		} catch (Exception e) {
			e.printStackTrace();
			
			model.addAttribute("errorMessage", "소장품정보를 가져오는중 에러가 발생했습니다");
			model.addAttribute("newItemDto", new NewItemDto());
			
			return "admin/newItem";
		}
		
		return "admin/itemModify";
	}
	
	//전시 수정기능
	@PostMapping(value = "/admin/item/{itemId}")
	public String itemUpdate(@Valid NewItemDto newItemDto, BindingResult bindingResult, Model model,
			@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "item/newItem";
		}
		
		if(itemImgFileList.get(0).isEmpty() && newItemDto.getId() == null) {
			model.addAttribute("errorMessage","첫번째 소장품 이미지는 필수입니다");
			return "item/newItem";
		}
		
		try {
			itemService.updateItem(newItemDto, itemImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "소장품 수정 중 에러가 발생했습니다");
			return "item/newItem";
		}
		
		return "redirect:/";
		
	}
	
	//수정시 이미지 삭제
	@PostMapping(value = "/item/deleteImg/{itemImgId}")
	public ResponseEntity<String> deleteImg(@PathVariable("itemImgId") Long itemImgId){
		
		try {
			itemService.deleteItemImg(itemImgId);
			return ResponseEntity.ok("이미지 삭제 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 실패");
		}
	}
	
	//예약 가능한 전시 리스트 페이지
	@GetMapping(value = {"/exhibition/list", "/exhibition/list/{page}"})
	public String itemListPage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 6);
		
		Page<Item> itemList = itemService.getAdminItemPage(itemSearchDto, pageable);
		
		model.addAttribute("itemList", itemList);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "/exhibition/list";
	}
	
	//전시 상세 페이지
	@GetMapping(value = "/exhibition/{itemId}")
	public String ExDtl(Model model, @PathVariable("itemId") Long itemId) {
		
		NewItemDto newItemDto = itemService.getItemDtl(itemId);
		model.addAttribute("exhibition", newItemDto);
		
		return "exhibition/Dtl";
	}
	
	//전시 삭제
	@DeleteMapping(value = "/item/deleteItem/{itemId}")
	public ResponseEntity<String> deleteItem(@PathVariable("itemId") Long itemId){
		
		try {
			itemService.deleteItem(itemId);
			return ResponseEntity.ok("전시가 삭제 되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전시 삭제 실패");
		}
	}
	
}
