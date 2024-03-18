package com.museum.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.museum.entity.Item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewItemDto {
	private Long id;
	
	@NotBlank(message = "전시명은 필수 입력 값입니다")
	private String itemNm;
	
	@NotBlank(message = "전시설명은 필수 입력 값입니다")
	private String itemDetail;
	
	@NotBlank(message = "전시날짜는 필수 입력값입니다")
	private String itemDate;
	
	private int stock;
	
	//전시소장품 이미지 정보를 저장 
	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
	
	//전시소장품 이미지 아이디를 저장 -> 수정시에 이미지 아이디를 담아둘 용도
	private List<Long> itemImgIds = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
			
	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}
	
	//엔티티를 dto로
	public static NewItemDto of (Item item) {
		return modelMapper.map(item, NewItemDto.class);
	}
	
}
