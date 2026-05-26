package com.raymond.bookingsystem.repository;


import java.time.LocalDate;
import java.util.List;
import com.raymond.bookingsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
    SELECT b FROM Booking b
    WHERE b.room.id = :roomId
    AND b.status = 'ACTIVE'
    AND b.checkInDate < :checkOutDate
    AND b.checkOutDate > :checkInDate
    """)
    List<Booking> findConflictingBookings(
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    );

}
