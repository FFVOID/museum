package com.museum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.museum.dto.BoardSearchDto;
import com.museum.entity.Board;
import com.museum.entity.QBoard;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
	
	private JPAQueryFactory queryFactory;
	
	public BoardRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("writer", searchBy)) {
			return QBoard.board.writer.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("title", searchBy)) {
			return QBoard.board.title.like("%" + searchQuery + "%");
		} else if(StringUtils.equals("content", searchBy)) {
			return QBoard.board.content.like("%" + searchQuery + "%");
		}
		
		return null;
	}
	
	@Override
	public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
		
		List<Board> content = queryFactory
								.selectFrom(QBoard.board)
								.where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
								.orderBy(QBoard.board.id.desc())
								.offset(pageable.getOffset())
								.limit(pageable.getPageSize())
								.fetch();
		
		long total = queryFactory.select(Wildcard.count).from(QBoard.board)
					.where(searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
					.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}
}
