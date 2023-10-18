package com.museum.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMemberDto {
	
	private String userId;
	
	@NotEmpty(message = "비밀번호를 확인해주세요")
	@Length(min = 8, max = 16, message = "비밀번호는 8 ~ 16자 사이로 입력해주세요")
	private String password;
	
}
