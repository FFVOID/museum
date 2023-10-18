package com.museum.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.museum.dto.ItemSearchDto;
import com.museum.dto.MainItemDto;
import com.museum.service.ItemService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final ItemService itemService;
	 
	@GetMapping(value = "/")
	public String getItemDtl(ItemSearchDto itemSearchDto,Optional<Integer>page , Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 3);
		Page<MainItemDto> items = itemService.getMainPage(pageable);
		
		model.addAttribute("items", items);
		
		return "main";
		
	}
	
}
