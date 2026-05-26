package com.raymond.bookingsystem.controllers;

import com.raymond.bookingsystem.model.Booking;
import com.raymond.bookingsystem.model.CreateCustomerRequest;
import com.raymond.bookingsystem.model.Customer;
import com.raymond.bookingsystem.model.Room;
import com.raymond.bookingsystem.service.BookingService;
import com.raymond.bookingsystem.service.CustomerService;
import com.raymond.bookingsystem.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class BookingWebController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;

    public BookingWebController(
            BookingService bookingService,
            CustomerService customerService,
            RoomService roomService
    ) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.roomService = roomService;
    }

    @GetMapping("/book-room")
    public String showBookingForm(
            @RequestParam Long roomId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkIn,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOut,

            Model model
    ) {
        Room room = roomService.getRoomById(roomId);

        model.addAttribute("room", room);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("pageTitle", "Boka rum " + room.getRoomNumber());

        return "booking-form";
    }

    @PostMapping("/book-room")
    public String submitBooking(
            @RequestParam Long roomId,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String password,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam int numOfGuests,
            Model model
    ) {
        Room room = roomService.getRoomById(roomId);

        Customer customer = customerService.createCustomer(
                new CreateCustomerRequest(name, email, phoneNumber, password)
        );

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setNumOfGuests(numOfGuests);

        Booking savedBooking = bookingService.createBooking(booking, customer.getId());

        model.addAttribute("booking", savedBooking);
        model.addAttribute("room", room);
        model.addAttribute("pageTitle", "Bokning bekräftad");

        return "booking-confirmation";
    }
}
