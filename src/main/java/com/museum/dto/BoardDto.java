package com.museum.dto;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.museum.entity.Board;
import com.museum.oauth.PrincipalDetails;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
	
	private Long id;
	
	private String writer; //글 작성자
	
	private String cmtWriter; //댓글 작성자
	
	@NotBlank(message = "글 제목은 필수 입력값입니다")
	private String title; //글 제목
	
	@NotBlank(message = "글 내용은 필수 입력값입니다")
	private String content;//글 내용
	
	private int count = 0;//조회수
	
	private LocalDateTime regTime;
	
	private List<BoardImgDto> boardImgList = new ArrayList<>(); //글작성 첨부 이미지
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	private List<Long> boardImgIds = new ArrayList<>();
	
	public Board createBoard() {
		return modelMapper.map(this, Board.class);
	}
	
	public static BoardDto createBoardDto(Principal principal) {
	    BoardDto boardDto = new BoardDto();
	    if (principal != null) {
	        String username = principal.getName(); // 현재 로그인한 사용자의 이름을 가져옴
	        boardDto.setWriter(username);
	    }
	    return boardDto;
	}
	
	public static BoardDto of(Board board) {
		return modelMapper.map(board, BoardDto.class);
	}
}
