package com.museum.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_img")
@Getter
@Setter
public class BoardImg {
	
	@Id
	@Column(name = "board_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName; //uuid 처리된 이미지파일 이름 
	
	private String oriImgName; //원본 이미지 이름
	
	private String imgUrl; //이미지 조회 경로
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board")
	private Board board;
	
	public void updateBoardImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
