package com.museum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museum.entity.BoardImg;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
	
	List<BoardImg> findByBoardIdOrderByIdAsc(Long boardId);
}
