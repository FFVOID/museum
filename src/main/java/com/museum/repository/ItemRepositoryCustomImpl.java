package com.museum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.museum.dto.ItemSearchDto;
import com.museum.dto.MainItemDto;
import com.museum.dto.QMainItemDto;
import com.museum.entity.Item;
import com.museum.entity.QItem;
import com.museum.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%" + searchQuery + "%");
		} else if (StringUtils.equals("createBy", searchBy)) {
			return QItem.item.createBy.like("%" + searchQuery + "%");
		} else if (StringUtils.equals("itemDate", searchBy)) {
	        return QItem.item.itemDate.eq(searchQuery);
	    }
		
		return null;
	}

	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		
		List<Item> content = queryFactory
							.selectFrom(QItem.item)
							.where(searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
							.orderBy(QItem.item.id.desc())
							.offset(pageable.getOffset())
							.limit(pageable.getPageSize())
							.fetch();
		
		long total = queryFactory.select(Wildcard.count).from(QItem.item)
				.where(searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
				
	}

	
	@Override
	public Page<MainItemDto> getMainItemPage(Pageable pageable) {
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		List<MainItemDto> content = queryFactory
				.select(
					new QMainItemDto(
							item.id,
							item.itemNm,
							item.itemDetail,
							item.itemDate,
							itemImg.imgUrl,
							itemImg.oriImgName)
					)
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.orderBy(item.id.desc())
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}
	
}
