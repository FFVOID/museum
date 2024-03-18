package com.museum.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.museum.dto.ReservedDto;
import com.museum.entity.Item;
import com.museum.repository.ItemRepository;
import com.museum.service.ReservationService;

@SpringBootTest
class ReservationTest2 {
		
	   	@Autowired
	    private ReservationService reservationService;

	    @Autowired
	    private ItemRepository itemRepository;

	    private final Object lock = new Object();

	    @Test
	    void testConcurrentReservations() throws InterruptedException {
	    	
	    	long startTime = System.nanoTime();
	    	
	        Item item = new Item();
	        item.setItemNm("전시명");
	        item.setItemDetail("전시설명");
	        item.setItemDate("전시날짜");
	        item.setStock(100);
	        itemRepository.save(item);

	        int numThreads = 10; // 테스트할 스레드 수
	        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

	        List<Long> reservationIds = new CopyOnWriteArrayList<>();
	        List<String> failedAttempts = new CopyOnWriteArrayList<>();
	        
	        //동기화 없는 예약 테스트
	        for (int i = 0; i < numThreads; i++) {
	            final String memberId = "member_" + i;
	            executor.submit(() -> {
	                try {
	                    ReservedDto reservedDto = new ReservedDto();
	                    reservedDto.setCount(1);
	                    reservedDto.setDate(null);
	                    reservedDto.setItemId(item.getId());
	                    reservedDto.setReservedNm("HI");

	                   
	                    Long reservationId = reservationService.reserved(reservedDto, memberId);
	                    
	                    if (reservationId != null) {
	                        System.out.println("동시성 테스트 성공: " + reservationId + ", 유저 아이디: " + memberId);
	                        reservationIds.add(reservationId);
	                    } else {
	                        System.err.println("예약 실패: 유저 아이디: " + memberId);
	                        failedAttempts.add(memberId);
	                    }
	                } catch (Exception e) {
	                    System.err.println("예약 시도 중 오류 발생. 유저 아이디: " + memberId + ", 오류: " + e.getMessage());
	                    failedAttempts.add(memberId);
	                }
	            });
	        }

	        executor.shutdown();
	        executor.awaitTermination(1, TimeUnit.HOURS);

	        System.out.println("성공한 예약 수: " + reservationIds.size());
	        System.out.println("실패한 예약 시도 수: " + failedAttempts.size());
	        
	        long endTime = System.nanoTime();
	        long duration = (endTime - startTime);

	       
	        double durationInSeconds = duration / 1_000_000_000.0;
	        System.out.println("동기화 없이 예약 처리하는 테스트 실행 시간: " + durationInSeconds + "초");

	        assertEquals(numThreads, reservationIds.size() + failedAttempts.size());
	        assertTrue(failedAttempts.isEmpty(), "일부 예약이 실패했습니다. 실패한 예약: " + failedAttempts);
	    }
}
