package com.museum.entity;

import java.util.ArrayList;
import java.util.List;

import com.museum.dto.NewItemDto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
	
	//전시 식별자 아이디
	@Id
	@Column(name="item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//전시 이름
	@Column(nullable = false)
	private String itemNm;
	
	//전시 기간
	@Column(nullable = false)
	private String itemDate;
	
	//남은인원
	private int stock;
	
	//전시 상세설명
	@Lob
	@Column(nullable = false, columnDefinition = "longtext")
	private String itemDetail;
	
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<ItemImg> itemImgs = new ArrayList<>();
	
	//수정된 전시 정보를 업데이트
	public void updateItem(NewItemDto newItemDto) {
		this.itemNm = newItemDto.getItemNm();
		this.itemDetail = newItemDto.getItemDetail();
		this.itemDate = newItemDto.getItemDate();
	
	}
	
	 // 예약 메서드
    public int newStock(int count) {
        if (stock >= count) {
            stock -= count; // 남은 인원 수를 감소
        } else {
           
        }
        
        return stock;
    }
	
	
}
