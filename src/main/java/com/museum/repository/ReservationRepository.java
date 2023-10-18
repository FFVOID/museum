package com.museum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.museum.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
	@Query("select r from Reservation r where r.Id = :Id")
	Reservation getReservationItem(@Param("Id") Long reservationId);
}
