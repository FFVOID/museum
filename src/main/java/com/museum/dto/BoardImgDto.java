package com.museum.dto;

import org.modelmapper.ModelMapper;

import com.museum.entity.BoardImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardImgDto {
	
	private Long id;
	
	private String imgName; //uuid 처리된 이미지파일 이름 
	
	private String oriImgName; //원본 이미지 이름
	
	private String imgUrl; //이미지 조회 경로
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static BoardImgDto of(BoardImg boardImg) {
		return modelMapper.map(boardImg, BoardImgDto.class);
	}
}
