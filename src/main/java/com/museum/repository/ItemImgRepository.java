package com.museum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museum.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
	
	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
