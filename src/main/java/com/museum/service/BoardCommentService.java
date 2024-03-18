package com.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.museum.dto.BoardCommentDto;
import com.museum.dto.BoardDto;
import com.museum.entity.Board;
import com.museum.entity.BoardComment;
import com.museum.repository.BoardCommentRepository;
import com.museum.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommentService {
	
	private final BoardCommentRepository boardCmtRepository;
	private final BoardRepository boardRepository;
	
	//댓글 등록
	public void saveComment(BoardComment boardComment) {
		
		boardCmtRepository.save(boardComment);
		
		Board board = boardComment.getBoard();
	    board.setCommentCount(board.getCommentCount() + 1);
	    boardRepository.save(board);
	}
	
	//댓글 가져오기
	@Transactional(readOnly = true)
	public List<BoardCommentDto> getBoardCmtDto(Long boardId) {
		
		List<BoardComment> boardCmtList = boardCmtRepository.findAllByBoardId(boardId);
		
		List<BoardCommentDto> boardCmtDtoList = new ArrayList<>();
		
		for(BoardComment boardCmt : boardCmtList) {
			boardCmtDtoList.add(BoardCommentDto.of(boardCmt));
		}
		
		return boardCmtDtoList;
	}
	
	public Page<BoardCommentDto> getBoardCmtPage(Long boardId, Pageable pageable) {
	    
		Page<BoardComment> boardComments = boardCmtRepository.findCmtByBoardId(boardId, pageable);
	    
	    return boardComments.map(comment -> new BoardCommentDto(comment));
	}
	
	public void deleteCmt(Long cmtId) {
        
		Optional<BoardComment> cmtOptional = boardCmtRepository.findById(cmtId);
        if (cmtOptional.isPresent()) {
            boardCmtRepository.delete(cmtOptional.get());
        } else {
        	
        }
    }
	
}
