package com.museum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.museum.dto.EmailDto;
import com.museum.service.MailService;
import com.museum.service.MemberService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailController {
	
	private final MailService mailService;
	
	private final MemberService memberService;
	
	@PostMapping("/email") // /mail로 들어오면 post방식으로 메일을 전송
    public @ResponseBody ResponseEntity MailSend(@RequestBody EmailDto emailDto,Model model) throws MessagingException{
    	
		boolean memberCheck = memberService.findMember(emailDto.getEmail());
    			
    	if(!memberCheck) {
    		return new ResponseEntity<>("이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
    	}		
        int number = mailService.sendMail(emailDto.getEmail());
        
        //인증번호
        String num = "" + number;
        System.out.println("인증번호==" + num);
        
        return new ResponseEntity<>(num, HttpStatus.OK);
    }
}
