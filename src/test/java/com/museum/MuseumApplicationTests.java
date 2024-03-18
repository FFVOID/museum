package com.museum;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.museum.service.MemberService;

@SpringBootTest
@Transactional
class MuseumApplicationTests {
	
	int threadCount = 100;
	
	ExecutorService executorService = Executors.newFixedThreadPool(30);
	
	CountDownLatch latch = new CountDownLatch(threadCount);
	
	@Autowired
	MemberService memberService;
	
	@Test
	void contextLoads() {
	}

}
