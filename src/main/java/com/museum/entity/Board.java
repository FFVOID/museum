package com.museum.entity;

import java.util.List;


import com.museum.dto.BoardDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board")
public class Board extends BaseTimeEntity {
	
	@Id
	@Column(name = "board_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String writer; //글 작성자
	
	private String title; //글 제목
	
	private String content;//글 내용
	
	private int count;//조회수
	
	@Column(name = "comment_count")
	private int commentCount; //댓글수
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	private List<BoardImg> boardImgs;
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	private List<BoardComment> boardComments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	public void updateBoard(BoardDto boardDto) {
		this.title = boardDto.getTitle();
		this.content = boardDto.getContent();
	}
	
}
