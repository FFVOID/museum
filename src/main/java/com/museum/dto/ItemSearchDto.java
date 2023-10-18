package com.museum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
	
	private String searchBy;
	private String searchQuery = "";
}
