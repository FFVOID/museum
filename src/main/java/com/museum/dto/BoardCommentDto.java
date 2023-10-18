package com.museum.dto;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.museum.entity.BoardComment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCommentDto {
	
	private Long id;
	
	private String cmtWriter;
	
	private String cmtContent;
	
	private LocalDateTime regTime;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public BoardComment createComment() {
		return modelMapper.map(this, BoardComment.class);
	}
	
	public static BoardCommentDto of(BoardComment boardComment) {
		return modelMapper.map(boardComment, BoardCommentDto.class);
	}
	
	
	public BoardCommentDto(BoardComment boardComment) {
        this.id = boardComment.getId();
        this.cmtContent = boardComment.getCmtContent();
        this.cmtWriter = boardComment.getCmtWriter();
        this.regTime = boardComment.getRegTime();
    }
	
}
