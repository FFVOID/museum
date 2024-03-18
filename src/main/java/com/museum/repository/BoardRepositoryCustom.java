package com.museum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museum.dto.BoardSearchDto;
import com.museum.entity.Board;

public interface BoardRepositoryCustom {
	
	Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
	
}
