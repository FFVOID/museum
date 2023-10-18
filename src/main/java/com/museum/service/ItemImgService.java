package com.museum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.museum.entity.BoardImg;
import com.museum.entity.ItemImg;
import com.museum.repository.ItemImgRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
	
	private String itemImgLocation = "C:/museum/item";
	//@Value("${itemImgLocation}")
	//private String itemImgLocation 배포용;
	
	private final ItemImgRepository itemImgRepository;
	private final FileService fileService;
	
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/img/item/" + imgName; 
		}
		
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg);
		
	}
	
	//이미지 업데이트 메소드
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
			
			if(!itemImgFile.isEmpty()) {
				ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
														.orElseThrow(EntityNotFoundException::new);
				
				if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
					fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
				}
				
				String oriImgName = itemImgFile.getOriginalFilename();
				String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
				String imgUrl = "/img/item/" + imgName;
				
				//★update쿼리문 실행 => 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 
				//그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지 않고 엔티티만 변경해주면 된다
				savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
	}
	
	public void deleteItemImg(Long itemImgId) {
		ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
				.orElseThrow(EntityNotFoundException::new);
		
		String oriImgName = null;
		String imgName = null;
		String imgUrl = null;
		
		savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
	}
}
