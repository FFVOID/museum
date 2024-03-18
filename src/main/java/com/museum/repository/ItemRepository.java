package com.museum.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.museum.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> , ItemRepositoryCustom{
	
}
