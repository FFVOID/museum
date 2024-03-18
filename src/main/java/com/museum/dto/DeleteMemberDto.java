package com.museum.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMemberDto {
	
	private String userId;
	
	@NotEmpty
	private String password;
	
}
