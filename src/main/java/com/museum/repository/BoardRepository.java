package com.museum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museum.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> , BoardRepositoryCustom{
	
}
