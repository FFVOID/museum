package com.museum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
	private String email; //ajax로 받은 이메일을 MailController로 전달
}
