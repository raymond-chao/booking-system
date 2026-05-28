package com.raymond.bookingsystem.controllers;

import com.raymond.bookingsystem.model.Booking;

import com.raymond.bookingsystem.model.Customer;
import com.raymond.bookingsystem.model.Room;
import com.raymond.bookingsystem.service.BookingService;
import com.raymond.bookingsystem.service.CustomerService;
import com.raymond.bookingsystem.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

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

    @GetMapping("/book-room/check-availability")
    @ResponseBody
    public Map<String, Object> checkRoomAvailability(
            @RequestParam Long roomId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkIn,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOut
    ) {
        if (!checkOut.isAfter(checkIn)) {
            return Map.of(
                    "available", false,
                    "message", "Utcheckningsdatum måste vara efter incheckningsdatum."
            );
        }

        boolean available = roomService.isRoomAvailable(roomId, checkIn, checkOut);

        if (available) {
            return Map.of(
                    "available", true,
                    "message", "Rummet är ledigt valda datum."
            );
        }

        return Map.of(
                "available", false,
                "message", "Rummet är redan bokat dessa datum. Välj andra datum."
        );
    }

    @PostMapping("/book-room")
    public String submitBooking(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            Model model,
            Authentication authentication
    ) {
        Room room = roomService.getRoomById(roomId);

        String email = authentication.getName();

        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kund inte hittad"));

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);

        Booking savedBooking = bookingService.createBooking(booking, customer.getId());

        model.addAttribute("booking", savedBooking);
        model.addAttribute("room", room);
        model.addAttribute("pageTitle", "Bokning bekräftad");

        return "booking-confirmation";
    }

    @GetMapping("/bookings")
    public String showFindBookingPage(Model model) {
        model.addAttribute("pageTitle", "Hitta din bokning");
        return "booking-search";
    }

    @PostMapping("/bookings/search")
    public String findBooking(
            @RequestParam String name,
            @RequestParam String bookingConfirmation,
            Model model
    ) {
        try {
            Booking booking = bookingService.findBookingForCustomer(name, bookingConfirmation);

            model.addAttribute("booking", booking);
            model.addAttribute("pageTitle", "Din bokning");

            return "booking-details";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Hitta din bokning");

            return "booking-search";
        }
    }

    @GetMapping("/bookings/edit/{id}")
    public String showEditBookingForm(
            @PathVariable Long id,
            Model model
    ) {
        Booking booking = bookingService.getBookingById(id);

        model.addAttribute("booking", booking);
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("pageTitle", "Ändra bokning");

        return "booking-edit";
    }

    @PostMapping("/bookings/update/{id}")
    public String updateBooking(
            @PathVariable Long id,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            Model model
    ) {
        try {
            Room room = roomService.getRoomById(roomId);

            Booking updatedBooking = new Booking();
            updatedBooking.setRoom(room);
            updatedBooking.setCheckInDate(checkInDate);
            updatedBooking.setCheckOutDate(checkOutDate);

            Booking savedBooking = bookingService.updateBooking(id, updatedBooking);

            model.addAttribute("booking", savedBooking);
            model.addAttribute("success", "Bokningen har uppdaterats.");
            model.addAttribute("pageTitle", "Din bokning");

            return "booking-details";
        } catch (RuntimeException e) {
            Booking booking = bookingService.getBookingById(id);

            model.addAttribute("booking", booking);
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Ändra bokning");

            return "booking-edit";
        }
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancelBooking(
            @PathVariable Long id,
            Model model
    ) {
        bookingService.cancelBooking(id);

        model.addAttribute("success", "Bokningen har avbokats.");
        model.addAttribute("pageTitle", "Bokning avbokad");

        return "booking-cancelled";
    }
}
