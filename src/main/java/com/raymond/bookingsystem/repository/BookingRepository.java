package com.raymond.bookingsystem.repository;

import com.raymond.bookingsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
