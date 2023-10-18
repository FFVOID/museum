package com.museum.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museum.dto.ItemSearchDto;
import com.museum.dto.MainItemDto;
import com.museum.entity.Item;

public interface ItemRepositoryCustom {
	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
	
	Page<MainItemDto> getMainItemPage(Pageable pageable);
}
