package com.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.museum.dto.ItemImgDto;
import com.museum.dto.ItemSearchDto;
import com.museum.dto.MainItemDto;
import com.museum.dto.NewItemDto;
import com.museum.entity.Board;
import com.museum.entity.Item;
import com.museum.entity.ItemImg;
import com.museum.repository.ItemImgRepository;
import com.museum.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
	
	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService;
	private final ItemImgRepository itemImgRepository;
	
	//전시 등록
	public Long saveItem(NewItemDto newItemDto, List<MultipartFile> itemImgFileList,Item items) throws Exception {
		
		Item item = newItemDto.createItem(); //dto -> entity
		item.setStock(100);
		itemRepository.save(item); //저장
		
		//이미지 등록
		for(int i=0; i<itemImgFileList.size(); i++) {
			
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);
			
			if(i == 0) {
				itemImg.setRepimgYn("Y");
			} else {
				itemImg.setRepimgYn("N");
			}
			
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	//전시정보가져오기
	@Transactional(readOnly = true)
	public NewItemDto getItemDtl(Long itemId) {
		
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		
		for(ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			
			itemImgDtoList.add(itemImgDto);
		}
		
		Item item = itemRepository.findById(itemId)
								  .orElseThrow(EntityNotFoundException::new);
		
		NewItemDto newItemDto = NewItemDto.of(item);
		
		newItemDto.setItemImgDtoList(itemImgDtoList);
		
		return newItemDto;
	}
	
	
	//전시 수정
	public Long updateItem(NewItemDto newItemDto, List<MultipartFile> itemImgFileList) throws Exception {
		
		Item item = itemRepository.findById(newItemDto.getId())
								 	.orElseThrow(EntityNotFoundException::new);
		
		item.updateItem(newItemDto);
		
		List<Long> itemImgIds = newItemDto.getItemImgIds();
		
		for(int i = 0; i<itemImgFileList.size(); i++) {
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	
	//검색 , 페이지처리
	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
		
		Page<Item> itemPage = itemRepository.getAdminItemPage(itemSearchDto, pageable);
		return itemPage;
	}
	
	//메인
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainPage(Pageable pageable){
		
		Page<MainItemDto> mainItemPage = itemRepository.getMainItemPage(pageable);
		
		return mainItemPage;
	}
	
	//수정시 이미지 삭제 메소드
	public void deleteItemImg(Long itemImgId) {
		
		itemImgService.deleteItemImg(itemImgId);
	}
	
	//전시 삭제
	public void deleteItem(Long itemId) {
		
		Optional<Item> itemOptional = itemRepository.findById(itemId); 
		
		if (itemOptional.isPresent()) {
			
			Item item = itemOptional.get();

	        if (item.getItemImgs() != null) {
	            itemImgRepository.deleteAll(item.getItemImgs());
	        }

	        itemRepository.delete(item);
        } else {
        	
        }
	}
}
