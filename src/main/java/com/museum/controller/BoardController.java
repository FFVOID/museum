package com.museum.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.museum.dto.BoardCommentDto;
import com.museum.dto.BoardDto;
import com.museum.dto.BoardSearchDto;
import com.museum.entity.Board;
import com.museum.entity.BoardComment;
import com.museum.entity.Member;
import com.museum.repository.BoardRepository;
import com.museum.repository.MemberRepository;
import com.museum.service.BoardCommentService;
import com.museum.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	private final BoardCommentService boardCmtService;
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	
	@GetMapping(value = "/boards/new")
	public String newBoardMain(Model model, Principal principal) {
		
		BoardDto boardDto = BoardDto.createBoardDto(principal);
		
		model.addAttribute("boardDto", boardDto);
		return "board/new";
	}
	
	@PostMapping(value = "/boards/new")
	public String newBoard(@Valid BoardDto boardDto, BindingResult bindingResult, Model model,
		@RequestParam("boardImgFile") List<MultipartFile> boardImgFileList, Board boards) {
		
		//유효성검사 에러체크
		if(bindingResult.hasErrors()) {
			System.out.println("유효성검사체크");
			return "board/new";
		}
		
		try {
			boardService.saveBoard(boardDto, boardImgFileList, boards);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "글 등록 중 에러가 발생했습니다");
		}
		
		return "redirect:/boards/list";
	}
	
	//게시판 글 리스트 페이지
	@GetMapping(value = {"/boards/list", "/boards/list/{page}"})
	public String boardListPage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer>page, Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 10);
		
		Page<Board> boardList = boardService.getBoardPage(boardSearchDto, pageable);
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardSearchDto", boardSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "/board/list";
	}
	
	//게시글 상세 페이지
	@GetMapping(value = "/boards/view/{boardId}")
	public String boardView(Model model, @PathVariable("boardId") Long boardId , Principal principal,
		@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
		
		BoardDto boardDto = boardService.getBoardDtl(boardId);
		String cmtWriter = principal.getName();
		
		boardDto.setCmtWriter(cmtWriter);
		
		Page<BoardCommentDto> boardCmtDtoPage = boardCmtService.getBoardCmtPage(boardId, PageRequest.of(page - 1, size));
		
		model.addAttribute("boardDto", boardDto);
		model.addAttribute("boardCmtDtoPage", boardCmtDtoPage);
		
		return "board/view";
	}
	
	//수정페이지
	@GetMapping(value = "/boards/modify/{boardId}")
	public String modifyBoard(@PathVariable("boardId") Long boardId, Model model) {
		
		try {
			BoardDto boardDto = boardService.getBoardDtl(boardId);
			model.addAttribute("boardDto", boardDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시판글을 가져오는 중 에러가 발생했습니다");
			model.addAttribute("boardDto", new BoardDto());
			
			return "board/list";
		}
		
		return "board/modify";
	}
	
	//글수정
	@PostMapping(value = "/boards/modify/{boardId}")
	public String updateBoard(@Valid BoardDto boardDto, BindingResult bindingResult, Model model,
			@RequestParam("boardImgFile") List<MultipartFile> boardImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "/board/modify";
		}
		
		try {
			boardService.updateBoard(boardDto, boardImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "글 수정 중 에러가 발생했습니다");
			return "main";
		}
		
		return "redirect:/boards/list";
	}
	
	//수정시 이미지 삭제
	@PostMapping(value = "/boards/deleteImg/{boardImgId}")
	public ResponseEntity<String> deleteImg(@PathVariable("boardImgId") Long boardImgId) {
		
		try {
			boardService.deleteImg(boardImgId);
			return ResponseEntity.ok("이미지 삭제 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 실패");
		}
	}
	
	//댓글 등록
	@PostMapping(value = "/saveComment")
	public String saveCmt(@RequestBody Map<String, Object> requestBody, @AuthenticationPrincipal Object principal) {
		
		String cmtWriter = (String) requestBody.get("cmtWriter");
		String cmtContent = (String) requestBody.get("cmtContent");
		String boardIds = (String) requestBody.get("boardId");
		Long boardId = Long.parseLong(boardIds);
		
		Member member = memberRepository.findByUserId(cmtWriter);
		Optional<Board> board = boardRepository.findById(boardId);
		
		BoardComment boardComment = new BoardComment();
		
		if (board.isPresent()) {
		    Board boards = board.get(); // Optional에서 Board 객체를 추출
		    boardComment.setBoard(boards);
		} else {
			
		}
		
		boardComment.setMember(member);
	    boardComment.setCmtWriter(cmtWriter);
	    boardComment.setCmtContent(cmtContent);
		
		boardCmtService.saveComment(boardComment);
		
		String redirectUrl = "/boards/view/" + boardId;
		
		return "redirect:" + redirectUrl;
	}
	
	//댓글삭제
	@DeleteMapping(value = "/boards/deleteCmt/{cmtId}")
	public ResponseEntity<String> deleteCmt(@PathVariable("cmtId") Long cmtId){
		
		try {
			boardService.deleteCmt(cmtId);
			return ResponseEntity.ok("댓글 삭제 완료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패");
		}
	}
	
	//글삭제
	@DeleteMapping(value = "/boards/deleteBoard/{boardId}")
	public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId){
		
		try {
			boardService.deleteBoard(boardId);
			return ResponseEntity.ok("글이 삭제 되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 삭제 실패");
		}
	}
	
}	
