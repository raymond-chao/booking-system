package com.raymond.bookingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Room {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @NotBlank(message = "Rumsnummer måste anges")
    private String roomNumber;

    @Min(value = 1, message = "Ett rum måste ha minst en säng")
    private int beds;


    @Min(value = 1, message = "Pris per natt måste vara större än 0")
    private int pricePerNight;


    protected Room() {
    }

    public Room(String roomNumber, int beds, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.beds = beds;
        this.pricePerNight = pricePerNight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotBlank(message = "Rumsnummer måste anges") String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(@NotBlank(message = "Rumsnummer måste anges") String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Min(value = 1, message = "Ett rum måste ha minst en säng")
    public int getBeds() {
        return beds;
    }

    public void setBeds(@Min(value = 1, message = "Ett rum måste ha minst en säng") int beds) {
        this.beds = beds;
    }

    @Min(value = 1, message = "Pris per natt måste vara större än 0")
    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(@Min(value = 1, message = "Pris per natt måste vara större än 0") int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
