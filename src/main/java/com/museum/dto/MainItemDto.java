package com.museum.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
	
	private Long id;
	
	private String itemNm;
	
	private String itemDetail;
	
	private String itemDate;
	
	private String imgUrl;
	
	private String oriImgName;
	
	@QueryProjection
	public MainItemDto(Long id, String itemNm, String itemDetail, String itemDate, String imgUrl, String oriImgName) {
		this.id = id;
		this.itemNm = itemNm;
		this.itemDetail = itemDetail;
		this.itemDate = itemDate;
		this.imgUrl = imgUrl;
		this.oriImgName = oriImgName;
	}
	
}
