package com.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.museum.dto.BoardDto;
import com.museum.dto.BoardImgDto;
import com.museum.dto.BoardSearchDto;
import com.museum.entity.Board;
import com.museum.entity.BoardComment;
import com.museum.entity.BoardImg;
import com.museum.repository.BoardCommentRepository;
import com.museum.repository.BoardImgRepository;
import com.museum.repository.BoardRepository;
import com.museum.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardImgService boardImgService;
	private final MemberRepository memberRepository;
	private final BoardImgRepository boardImgRepository;
	private final BoardCommentService boardCommentService;
	private final BoardCommentRepository boardCmtRepository;
	
	//글등록
	public Long saveBoard(BoardDto boardDto, List<MultipartFile> boardImgFileList, Board boards) throws Exception {
		Board board = boardDto.createBoard();
		board.setMember(memberRepository.findByUserId(board.getWriter()));
		boardRepository.save(board);
		
		for(int i = 0; i<boardImgFileList.size(); i++) {
			
			BoardImg boardImg = new BoardImg();
			boardImg.setBoard(board);
			
			boardImgService.saveBoardImg(boardImg, boardImgFileList.get(i));
		}
		
		return board.getId();
	}
	
	//게시판 검색, 페이지 처리
	@Transactional(readOnly = true)
	public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
		Page<Board> boardPage = boardRepository.getBoardPage(boardSearchDto, pageable);
		return boardPage;
	}
	
	//게시글 정보 가져오기
	@Transactional(readOnly = true)
	public BoardDto getBoardDtl(Long boardId) {
		
		List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
		
		List<BoardImgDto> boardImgDtoList = new ArrayList<>();
		for(BoardImg boardImg : boardImgList) {
			BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
			
			boardImgDtoList.add(boardImgDto);
		}
		
		Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
		
		board.setCount(board.getCount() + 1);
	    boardRepository.save(board);
		
		BoardDto boardDto = BoardDto.of(board);
		
		boardDto.setBoardImgList(boardImgDtoList);
		
		return boardDto;
	}
	
	//수정
	public Long updateBoard(BoardDto boardDto, List<MultipartFile> boardImgFileList) throws Exception {
		Board board = boardRepository.findById(boardDto.getId())
									.orElseThrow(EntityNotFoundException::new);
		
		board.updateBoard(boardDto);
		
		board.setCount(board.getCount() + 1);
		
		List<Long> boardImgIds = boardDto.getBoardImgIds();
		
		if(boardImgFileList != null) {
			for(int i=0; i<boardImgFileList.size();i++) {
				boardImgService.updateBoardImg(boardImgIds.get(i), boardImgFileList.get(i));
			}
		} 
		
		return board.getId();
	}
	
	//수정시 이미지 삭제 메소드
	public void deleteImg(Long boardImgId) {
		
		boardImgService.deleteBoardImg(boardImgId);
	}
	
	//댓글 삭제
	public void deleteCmt(Long cmtId) {
		boardCommentService.deleteCmt(cmtId);
	}
	
	//글삭제
	public void deleteBoard(Long boardId) {
		Optional<Board> boardOptional = boardRepository.findById(boardId);
		
		if (boardOptional.isPresent()) {
			
			Board board = boardOptional.get();

	        if (board.getBoardImgs() != null) {
	            boardImgRepository.deleteAll(board.getBoardImgs());
	        }

	        if (board.getBoardComments() != null) {
	            boardCmtRepository.deleteAll(board.getBoardComments());
	        }

	        boardRepository.delete(board);
        } else {
        	
        }
	}
}
