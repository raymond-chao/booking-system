package com.raymond.bookingsystem.model;

import com.raymond.bookingsystem.repository.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Check-in date cannot be empty")
    @FutureOrPresent(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be empty")
    @FutureOrPresent(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    private int numOfGuests;
    private String bookingConfirmation;

    @ManyToOne(optional = false)
    private Room room;

    @ManyToOne(optional = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public String getBookingConfirmation() {
        return bookingConfirmation;
    }

    public void setBookingConfirmation(String bookingConfirmation) {
        this.bookingConfirmation = bookingConfirmation;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BookingStatus getStatus() {return status;}

    public void setStatus(BookingStatus status) {this.status = status;}
}
