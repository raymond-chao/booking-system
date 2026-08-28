package com.raymond.bookingsystem.service;

import com.raymond.bookingsystem.model.Booking;
import com.raymond.bookingsystem.model.Customer;
import com.raymond.bookingsystem.model.Room;
import com.raymond.bookingsystem.repository.BookingRepository;
import com.raymond.bookingsystem.model.BookingStatus;
import com.raymond.bookingsystem.repository.CustomerRepository;
import com.raymond.bookingsystem.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final CustomerRepository customerRepository;

    private final RoomRepository roomRepository;
    
    public BookingService(BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    //Uppdaterad booking med customer
    @Transactional
    public Booking createBooking(Booking booking, Long customerId) {

        validateDates(booking.getCheckInDate(), booking.getCheckOutDate());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Room room = roomRepository.findById(booking.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Rummet hittades inte"));

        validateGuests(booking.getNumOfGuests(), room.getBeds());

        booking.setCustomer(customer);
        booking.setRoom(room);

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Rummet är redan bokat dessa datum.");
        }

        booking.setBookingConfirmation(generateUniqueBookingConfirmation());

        booking.setStatus(BookingStatus.ACTIVE);
    
        return bookingRepository.save(booking);
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new RuntimeException("Utcheckningsdatum måste vara efter incheckningsdatum.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new RuntimeException("Incheckningsdatum kan inte vara i det förflutna.");
        }
    }

    private void validateGuests(int numOfGuests, int beds) {
        if (numOfGuests < 1) {
            throw new RuntimeException("Antal gäster måste vara minst 1.");
        }
        if (numOfGuests > beds) {
            throw new RuntimeException("Antal gäster överstiger antalet sängar i rummet.");
        }
    }

    private String generateUniqueBookingConfirmation() {
        String confirmationNumber;

        do {
            confirmationNumber = String.valueOf(
                    ThreadLocalRandom.current().nextLong(100000, 1000000)
            );
        } while (bookingRepository.existsByBookingConfirmation(confirmationNumber));

        return confirmationNumber;
    }

    @Transactional
    public Booking updateBooking(Long id, Booking updatedBooking) {

        validateDates(updatedBooking.getCheckInDate(), updatedBooking.getCheckOutDate());

        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bokning hittades inte"));

        Room room = roomRepository.findById(updatedBooking.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Rummet hittades inte"));

        validateGuests(updatedBooking.getNumOfGuests(), room.getBeds());

        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(
                        room.getId(),
                        updatedBooking.getCheckInDate(),
                        updatedBooking.getCheckOutDate()
                );

        boolean hasOtherConflicts = conflicts.stream()
                .anyMatch(b -> !b.getId().equals(id));

        if (hasOtherConflicts) {
            throw new RuntimeException("Datumkonflikt. Rummet är redan bokat under valda datum.");
        }

        existing.setCheckInDate(updatedBooking.getCheckInDate());
        existing.setCheckOutDate(updatedBooking.getCheckOutDate());
        existing.setNumOfGuests(updatedBooking.getNumOfGuests());
        existing.setRoom(room);

        return bookingRepository.save(existing);
    }

    public Booking findBookingForCustomer(String customerName, String bookingConfirmation) {
        return bookingRepository
                .findByBookingConfirmationAndCustomerNameIgnoreCase(bookingConfirmation, customerName)
                .orElseThrow(() -> new RuntimeException("Ingen bokning hittades med angivet namn och bokningsnummer."));
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bokning hittades inte"));
    }
    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Bokning hittades inte"));

        bookingRepository.delete(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}