package com.museum.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.museum.entity.BoardImg;
import com.museum.repository.BoardImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {
	
	private String boardImgLocation = "C:/museum/board";
	
	private final BoardImgRepository boardImgRepository;
	private final FileService fileService;
	
	public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception{
		
		String oriImgName = boardImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
			imgUrl = "/img/board/" + imgName;
		}
		
		boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
		boardImgRepository.save(boardImg);
	}
	
	public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception {
		
		if(!boardImgFile.isEmpty()) {
			BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
													.orElseThrow(EntityNotFoundException::new);
			
			if(!StringUtils.isEmpty(savedBoardImg.getImgName())) {
				fileService.deleteFile(boardImgLocation + "/" + savedBoardImg.getImgName());
			}
			
			String oriImgName = boardImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
			String imgUrl = "/img/board/" + imgName;
			
			savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);
		}
	}
	
	public void deleteBoardImg(Long boardImgId) {
		
		BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
										 .orElseThrow(EntityNotFoundException::new);
		
		String oriImgName = null;
		String imgName = null;
		String imgUrl = null;
		
		savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);
	}
	
}
