package com.museum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.museum.entity.BoardComment;
import com.museum.entity.Member;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>{
	
	BoardComment findByBoardId(Long boardId);
	
	List<BoardComment> findAllByBoardId(Long boardId);
	
	@Query("SELECT c FROM BoardComment c WHERE c.board.id = :boardId ORDER BY c.id DESC")
	Page<BoardComment> findCmtByBoardId(@Param("boardId") Long boardId, Pageable pageable);
	

}
