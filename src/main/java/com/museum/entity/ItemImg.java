package com.museum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
@ToString
public class ItemImg {
	
	@Id
	@Column(name = "item_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repimgYn;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "item_id")
	private Item item;
	
	public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
