package com.raymond.bookingsystem.service;

import com.raymond.bookingsystem.model.Booking;
import com.raymond.bookingsystem.repository.BookingRepository;
import com.raymond.bookingsystem.repository.BookingStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {

        if (booking.getCheckOutDate().isBefore(booking.getCheckInDate())) {
            throw new RuntimeException(
                    "Slutdatum kan inte vara före startdatum."
            );
        }

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Rummet är redan bokat dessa datum.");
        }

        booking.setBookingConfirmation(booking.getRoom().getId() + booking.getCheckInDate().toString());

        booking.setStatus(BookingStatus.ACTIVE);

        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {

        if (updatedBooking.getCheckOutDate().isBefore(updatedBooking.getCheckInDate())) {
            throw new RuntimeException("Slutdatum kan inte vara före startdatum.");
        }

        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bokning hittades inte"));

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(
                        updatedBooking.getRoom().getId(),
                        updatedBooking.getCheckInDate(),
                        updatedBooking.getCheckOutDate()
                );

        boolean hasOtherConflicts = conflicts.stream()
                .anyMatch(b -> !b.getId().equals(id));

        if (hasOtherConflicts) {
            throw new RuntimeException("Datumkonflikt.");
        }

        existing.setCheckInDate(updatedBooking.getCheckInDate());
        existing.setCheckOutDate(updatedBooking.getCheckOutDate());
        existing.setRoom(updatedBooking.getRoom());

        return bookingRepository.save(existing);
    }

    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Bokning hittades inte"));

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}